package com.example.carespawbe.service.Shop;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.linear.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GM11Service {

    public double[] gm11(List<Double> data, Integer predictN, Integer trainLen) {
        if (data == null || data.size() < 2) {
            throw new IllegalArgumentException("data phải có ít nhất 2 phần tử");
        }

        final int nOriginal = data.size();
        final int pN = (predictN == null) ? 5 : Math.max(0, predictN);

        // trainLen null -> floor(0.8*n)
        int tLen = (trainLen == null) ? (int) Math.floor(nOriginal * 0.8) : trainLen;
        if (tLen < 2) tLen = 2;
        if (tLen > nOriginal) tLen = nOriginal;

        // x0 train (null/NaN/Inf -> 0)
        double[] x0Train = new double[tLen];
        for (int i = 0; i < tLen; i++) x0Train[i] = safeNum(data.get(i));

        // ✅ guard: dữ liệu phẳng (all same) -> dễ singular -> forecast naive
        if (isAllSame(x0Train)) {
            return naiveForecast(data, pN);
        }

        // AGO: x1 = cumsum(x0)
        double[] x1 = new double[tLen];
        double sum = 0.0;
        for (int i = 0; i < tLen; i++) {
            sum += x0Train[i];
            x1[i] = sum;
        }

        // z1[i] = (x1[i] + x1[i-1]) / 2, i=1..tLen-1
        if (tLen - 1 < 1) {
            return naiveForecast(data, pN);
        }
        double[] z1 = new double[tLen - 1];
        for (int i = 1; i < tLen; i++) {
            z1[i - 1] = (x1[i] + x1[i - 1]) / 2.0;
        }

        // B = [ -z1, 1 ], Y = x0[1:]
        double[][] bData = new double[tLen - 1][2];
        double[] yData = new double[tLen - 1];
        for (int i = 0; i < tLen - 1; i++) {
            bData[i][0] = -z1[i];
            bData[i][1] = 1.0;
            yData[i] = x0Train[i + 1];
        }

        RealMatrix B = MatrixUtils.createRealMatrix(bData);
        RealVector Y = new ArrayRealVector(yData);

        // ✅ Solve a,b: QR -> fallback SVD (tách catch để tránh lỗi subclass trong multi-catch)
        RealVector params;
        try {
            params = new QRDecomposition(B).getSolver().solve(Y);
        } catch (SingularMatrixException ex) {
            params = new SingularValueDecomposition(B).getSolver().solve(Y);
        } catch (MathIllegalArgumentException ex) {
            params = new SingularValueDecomposition(B).getSolver().solve(Y);
        } catch (Exception ex) {
            // bất kỳ lỗi lạ nào -> không 500
            return naiveForecast(data, pN);
        }

        double a = params.getEntry(0);
        double b = params.getEntry(1);

        // ✅ a,b không hợp lệ -> fallback
        if (!Double.isFinite(a) || !Double.isFinite(b) || Math.abs(a) < 1e-12) {
            return naiveForecast(data, pN);
        }

        // predict_x1(k) = (x0[0] - b/a)*exp(-a*(k-1)) + b/a
        int totalLen = nOriginal + pN;
        double[] x1Forecast = new double[totalLen];
        for (int k = 1; k <= totalLen; k++) {
            x1Forecast[k - 1] = (x0Train[0] - b / a) * Math.exp(-a * (k - 1)) + (b / a);
        }

        // x0_forecast = diff(x1_forecast, prepend=x0[0])
        double[] x0Forecast = new double[totalLen];
        x0Forecast[0] = x0Train[0];
        for (int i = 1; i < totalLen; i++) {
            x0Forecast[i] = x1Forecast[i] - x1Forecast[i - 1];
        }

        // nếu forecast ra NaN/Inf -> fallback
        for (double v : x0Forecast) {
            if (!Double.isFinite(v)) return naiveForecast(data, pN);
        }

        return x0Forecast;
    }

    // ----------------- helpers -----------------

    private double safeNum(Double v) {
        if (v == null) return 0.0;
        double x = v;
        return Double.isFinite(x) ? x : 0.0;
    }

    private boolean isAllSame(double[] arr) {
        if (arr.length < 2) return true;
        double first = arr[0];
        for (double v : arr) {
            if (Math.abs(v - first) > 1e-9) return false;
        }
        return true;
    }

    private double[] naiveForecast(List<Double> data, int predictN) {
        int n = data.size();
        int pN = Math.max(0, predictN);

        double[] out = new double[n + pN];
        for (int i = 0; i < n; i++) out[i] = safeNum(data.get(i));

        double last = out[n - 1];
        for (int i = n; i < n + pN; i++) out[i] = last;

        return out;
    }
}
