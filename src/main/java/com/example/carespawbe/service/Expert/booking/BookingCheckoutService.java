package com.example.carespawbe.service.Expert.booking;

import com.example.carespawbe.dto.Expert.booking.*;
import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.entity.Expert.AppointmentEntity;
import com.example.carespawbe.entity.Expert.AvailabilitySlotEntity;
import com.example.carespawbe.entity.Expert.PetEntity;
import com.example.carespawbe.entity.Forum.ForumPostTypeEntity;
import com.example.carespawbe.entity.Shop.CategoryEntity;
import com.example.carespawbe.entity.Shop.PaymentEntity;
import com.example.carespawbe.repository.Auth.UserRepository;
import com.example.carespawbe.repository.Expert.AppointmentRepository;
import com.example.carespawbe.repository.Expert.AvailabilitySlotRepository;
import com.example.carespawbe.repository.Expert.PetRepository;
import com.example.carespawbe.repository.Forum.ForumPostCategoryRepository;
import com.example.carespawbe.repository.Forum.ForumPostTypeRepository;
import com.example.carespawbe.repository.Shop.CategoryRepository;
import com.example.carespawbe.repository.Shop.PaymentRepository;
import com.example.carespawbe.service.Expert.common.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BookingCheckoutService {
    private final UserRepository userRepository;
    private final AvailabilitySlotRepository slotRepository;
    private final AppointmentRepository appointmentRepository;

    private final PetRepository petRepository;
    private final ForumPostTypeRepository typeRepository;

    private final PaymentRepository paymentRepository;

    private final FileStorageService fileStorageService;
    private final PaymentGatewayService paymentGatewayService;

    @Transactional
    public BookingCheckoutResult checkout(BookingCheckoutRequest req, MultipartFile petImage, Long userId) {
        validate(req);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 1) Resolve pet (select/create)
        PetEntity pet = resolvePet(req.getPet(), petImage, user);

        // 2) Lock slot to avoid double booking
        AvailabilitySlotEntity slot = slotRepository.findById(req.getSlotId())
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        // ✅ NEW: prevent expert booking their own slot
        if (slot.getExpert() != null
                && slot.getExpert().getUser() != null
                && slot.getExpert().getUser().getId() != null
                && slot.getExpert().getUser().getId().equals(userId)) {
            throw new RuntimeException("You cannot book an appointment with yourself.");
        }

        if (slot.getBooked() == null || slot.getBooked() != 0) {
            throw new RuntimeException("Slot is not available");
        }
        if (appointmentRepository.existsBySlot_Id(slot.getId())) {
            throw new RuntimeException("Slot already has an appointment");
        }

        slot.setBooked(1);
        slotRepository.save(slot);

        // 3) Create appointment
        AppointmentEntity app = new AppointmentEntity();
        app.setStatus(0); // pending
        app.setUserNote(req.getUserNote());
        app.setSlot(slot);
        app.setUser(user);
        app.setPet(pet);
        app.setExpert(slot.getExpert());
        app.setPrice(slot.getPrice());

        app = appointmentRepository.save(app);

        // 4) Create payment
        PaymentEntity payment = new PaymentEntity();
        payment.setPaymentMethod(req.getPayment().getMethod());

//        double amountBefore = slot.getPrice() != null ? slot.getPrice().doubleValue() : 0.0;
//        int coinUsed = req.getPayment().getCoinUsed() != null ? req.getPayment().getCoinUsed() : 0;

//        double amountAfter = amountBefore; // TODO: trừ coin nếu có
//        payment.setAmount_before(amountBefore);
//        payment.setPaymentCoinUsed(coinUsed);
//        payment.setAmount_after(amountAfter);
        // set price
        payment.setPricePayment(100000.0);

//        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentCreatedAt(LocalDate.now());
//        payment.setPaymentUpdatedAt(null);

        payment = paymentRepository.save(payment);

        // 5) Payment url / cash handling
        String method = req.getPayment().getMethod();
        String paymentUrl;

        if ("cash".equalsIgnoreCase(method)) {
            paymentRepository.save(payment);
            paymentUrl = null;
        } else {
            paymentUrl = paymentGatewayService.createPaymentUrl(payment, app);
        }

        return new BookingCheckoutResult(app.getId(), payment.getPaymentId(), paymentUrl);
    }

    private void validate(BookingCheckoutRequest req) {
        if (req == null) throw new RuntimeException("Request is required");
        if (req.getSlotId() == null) throw new RuntimeException("slotId is required");
        if (req.getPet() == null || req.getPet().getMode() == null) throw new RuntimeException("pet.mode is required");
        if (req.getPayment() == null || req.getPayment().getMethod() == null) throw new RuntimeException("payment.method is required");
//        if (req.getPayment().getAgreed() == null || !req.getPayment().getAgreed()) throw new RuntimeException("Must agree to proceed");
    }

    private PetEntity resolvePet(PetSelection petSel, MultipartFile petImage, UserEntity user) {
        String mode = petSel.getMode();

        if ("select".equalsIgnoreCase(mode)) {
            if (petSel.getSelectedPetId() == null) throw new RuntimeException("selectedPetId is required");

            PetEntity pet = petRepository.findById(petSel.getSelectedPetId())
                    .orElseThrow(() -> new RuntimeException("Pet not found"));

            if (pet.getUser() == null || !pet.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("Pet does not belong to user");
            }
            return pet;
        }

        if (!"create".equalsIgnoreCase(mode)) throw new RuntimeException("Invalid pet mode");

        PetCreateInfo c = petSel.getCreate();
        if (c == null) throw new RuntimeException("pet.create is required");
        if (c.getSpeciesId() == null) throw new RuntimeException("speciesId is required");
        if (c.getName() == null || c.getName().isBlank()) throw new RuntimeException("pet name is required");
        if (c.getWeight() == null) throw new RuntimeException("weight is required");
        if (c.getDateOfBirth() == null) throw new RuntimeException("dateOfBirth is required");

        ForumPostTypeEntity species = typeRepository.findById(c.getSpeciesId())
                .orElseThrow(() -> new RuntimeException("Species not found"));

        PetEntity pet = new PetEntity();
        pet.setUser(user);
        pet.setSpecies(species);

        pet.setName(c.getName());
        pet.setBreed(c.getBreed());
        pet.setGender(c.getGender());
        pet.setDateOfBirth(c.getDateOfBirth());

        pet.setDescription(c.getDescription());
        pet.setMicrochipId(c.getMicrochipId());
        pet.setAllergies(c.getAllergies());
        pet.setChronic_diseases(c.getChronicDiseases());
        pet.setWeight(c.getWeight());

        if (petImage != null && !petImage.isEmpty()) {
            String imageUrl = fileStorageService.storePetImage(petImage, user.getId());
            pet.setImageUrl(imageUrl);
        }

        return petRepository.save(pet);
    }
//    private final UserRepository userRepository;
//    private final AvailabilitySlotRepository slotRepository;
//    private final AppointmentRepository appointmentRepository;
//
//    private final PetRepository petRepository;
//    private final CategoryRepository categoryRepository;
//
//    private final PaymentRepository paymentRepository;
//
//    private final FileStorageService fileStorageService;
//    private final PaymentGatewayService paymentGatewayService;
//
//    @Transactional
//    public BookingCheckoutResult checkout(BookingCheckoutRequest req, MultipartFile petImage, Long userId) {
//        validate(req);
//        UserEntity user = userRepository.findById(userId).orElse(null);
//        // 1) Resolve pet (select/create)
//        PetEntity pet = resolvePet(req.getPet(), petImage, user);
//
//        // 2) Lock slot to avoid double booking
//        AvailabilitySlotEntity slot = slotRepository.findById(req.getSlotId())
//                .orElseThrow(() -> new RuntimeException("Slot not found"));
//
//        // slot must be empty
//        if (slot.getBooked() == null || slot.getBooked() != 0) {
//            throw new RuntimeException("Slot is not available");
//        }
//
//        // extra guard: if appointment already exists for this slot
//        if (appointmentRepository.existsBySlot_Id(slot.getId())) {
//            throw new RuntimeException("Slot already has an appointment");
//        }
//
//        // mark slot booked immediately inside TX
//        slot.setBooked(1);
//        slotRepository.save(slot);
//
//        // 3) Create appointment
//        AppointmentEntity app = new AppointmentEntity();
//        app.setStatus(0); // pending
//        app.setUserNote(req.getUserNote());
//        app.setSlot(slot);
//        app.setUser(user);
//        app.setPet(pet);
//        app.setExpert(slot.getExpert());
//        app.setPrice(slot.getPrice()); // lấy price từ slot theo entity bạn đưa
//
//        app = appointmentRepository.save(app);
//
//        // 4) Create payment entity (independent, no order)
//        PaymentEntity payment = new PaymentEntity();
//        payment.setPayment_method(req.getPayment().getMethod());
//
//        double amountBefore = slot.getPrice() != null ? slot.getPrice().doubleValue() : 0.0;
//
//        int coinUsed = req.getPayment().getCoinUsed() != null ? req.getPayment().getCoinUsed() : 0;
//        // TODO: nếu bạn có quy đổi coin -> tiền, xử lý ở đây
//        double amountAfter = amountBefore;
//
//        payment.setAmount_before(amountBefore);
//        payment.setPaymentCoinUsed(coinUsed);
//        payment.setAmount_after(amountAfter);
//        payment.setPaymentStatus(PaymentStatus.PENDING);
//        payment.setPaymentCreatedAt(LocalDate.now());
//        payment.setPaymentUpdatedAt(null);
//
//        payment = paymentRepository.save(payment);
//
//        // 5) Payment url / cash handling
//        String method = req.getPayment().getMethod();
//        String paymentUrl;
//
//        if ("cash".equalsIgnoreCase(method)) {
//            payment.setPaymentStatus(PaymentStatus.PAID);
//            payment.setPaymentUpdatedAt(LocalDate.now());
//            paymentRepository.save(payment);
//            paymentUrl = null;
//        } else {
//            paymentUrl = paymentGatewayService.createPaymentUrl(payment, app);
//        }
//
//        return new BookingCheckoutResult(app.getId(), payment.getPaymentId(), paymentUrl);
//    }
//
//    private void validate(BookingCheckoutRequest req) {
//        if (req == null) throw new RuntimeException("Request is required");
//        if (req.getSlotId() == null) throw new RuntimeException("slotId is required");
//        if (req.getPet() == null || req.getPet().getMode() == null) throw new RuntimeException("pet.mode is required");
//        if (req.getPayment() == null || req.getPayment().getMethod() == null) throw new RuntimeException("payment.method is required");
//        if (req.getPayment().getAgreed() == null || !req.getPayment().getAgreed()) throw new RuntimeException("Must agree to proceed");
//    }
//
//    private PetEntity resolvePet(PetSelection petSel, MultipartFile petImage, UserEntity user) {
//        String mode = petSel.getMode();
//
//        if ("select".equalsIgnoreCase(mode)) {
//            if (petSel.getSelectedPetId() == null) throw new RuntimeException("selectedPetId is required");
//            PetEntity pet = petRepository.findById(petSel.getSelectedPetId())
//                    .orElseThrow(() -> new RuntimeException("Pet not found"));
//
//            if (!pet.getUser().getId().equals(user.getId())) {
//                throw new RuntimeException("Pet does not belong to user");
//            }
//            return pet;
//        }
//
//        if (!"create".equalsIgnoreCase(mode)) throw new RuntimeException("Invalid pet mode");
//
//        PetCreateInfo c = petSel.getCreate();
//        if (c == null) throw new RuntimeException("pet.create is required");
//        if (c.getSpeciesId() == null) throw new RuntimeException("speciesId is required");
//        if (c.getName() == null || c.getName().isBlank()) throw new RuntimeException("pet name is required");
//
//        CategoryEntity species = categoryRepository.findById(c.getSpeciesId())
//                .orElseThrow(() -> new RuntimeException("Species not found"));
//
//        PetEntity pet = new PetEntity();
//        pet.setUser(user);
//        pet.setSpecies(species);
//
//        pet.setName(c.getName());
//        pet.setBreed(c.getBreed());
//        pet.setGender(c.getGender());
//        pet.setDateOfBirth(c.getDateOfBirth());
//        pet.setDescription(c.getDescription());
//        pet.setMicrochipId(c.getMicrochipId());
//        pet.setAllergies(c.getAllergies());
//        pet.setChronic_diseases(c.getChronicDiseases());
//        pet.setWeight(c.getWeight());
//
//        if (petImage != null && !petImage.isEmpty()) {
//            String imageUrl = fileStorageService.storePetImage(petImage, user.getId());
//            pet.setImageUrl(imageUrl);
//        }
//
//        return petRepository.save(pet);
//    }
}


