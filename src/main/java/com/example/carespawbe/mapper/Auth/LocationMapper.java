//package com.example.carespawbe.mapper.Auth;
//
//import com.example.carespawbe.dto.Location.DistrictResponse;
//import com.example.carespawbe.dto.Location.ProvinceResponse;
//import com.example.carespawbe.dto.Location.WardResponse;
//import com.example.carespawbe.entity.Auth.DistrictEntity;
//import com.example.carespawbe.entity.Auth.ProvinceEntity;
//import com.example.carespawbe.entity.Auth.WardEntity;
//
//public class LocationMapper {
//
//    private LocationMapper() {}
//
//    public static ProvinceResponse toProvinceResponse(ProvinceEntity e) {
//        if (e == null) return null;
//
//        ProvinceResponse dto = new ProvinceResponse();
//        dto.setProvinceId(e.getProvinceId());
//        dto.setProvinceCode(Math.toIntExact(e.getProvinceCode())); // int
//        dto.setProvinceName(e.getProvinceName());
//        return dto;
//    }
//
//    public static DistrictResponse toDistrictResponse(DistrictEntity e) {
//        if (e == null) return null;
//
//        DistrictResponse dto = new DistrictResponse();
//        dto.setDistrictId(e.getDistrictId());
//        dto.setDistrictCode(Math.toIntExact(e.getDistrictCode())); // int
//        dto.setDistrictName(e.getDistrictName());
//
//        ProvinceEntity p = e.getProvince();
//        if (p != null) {
////            dto.setProvinceId(p.getProvinceId());
//            dto.setProvinceCode(Math.toIntExact(p.getProvinceCode())); // int
//        }
//        return dto;
//    }
//
//    public static WardResponse toWardResponse(WardEntity e) {
//        if (e == null) return null;
//
//        WardResponse dto = new WardResponse();
//        dto.setWardId(e.getWardId());
//        dto.setWardCode(Math.toIntExact(e.getWardCode())); // int
//        dto.setWardName(e.getWardName());
//
//        DistrictEntity d = e.getDistrict();
//        if (d != null) {
////            dto.setDistrictId(d.getDistrictId());
//            dto.setDistrictCode(Math.toIntExact(d.getDistrictCode())); // int
//        }
//        return dto;
//    }
//}
