package Utils;

import java.util.Random;

// 입력층 -> 은닉층 가중치 계산
public class HiddenLayer {

    public int numOfInput; // 입려층 수
    public int numOfOut; // 출력층 수
    public double[][] W; // 가중치
    public double[] b;


    public HiddenLayer(int numOfInput, int numOfOut, double[][] W, double[] b, Random rng) {
        if (rng == null) rng = new Random(123);  // seed random
        // 가중치가 생성되어 있지 않다면
        if (W == null) {
            W = new double[numOfOut][numOfInput];
            double w_ = 1. / numOfInput;

            for (int j = 0; j < numOfOut; j++) {
                for (int i = 0; i < numOfInput; i++) {
                    W[j][i] = uniform(-w_, w_, rng);  // 초기 가중치 랜덤 설정
                }
            }
        }

        if (b == null) b = new double[numOfOut];

        this.numOfInput = numOfInput;
        this.numOfOut = numOfOut;
        this.W = W;
        this.b = b;
    }

    // 활성함수 시그모이드
    private double sigmoid(double x) {
        return 1. / (1. + Math.pow(Math.E, -x));
    }

    private double dsigmoid(double y) {
        return y * (1. - y);
    }

    public double[] output(double[] x) {
        double[] y = new double[numOfOut];
        for (int j = 0; j < numOfOut; j++) {
            double preActivation_ = 0.;
            for (int i = 0; i < numOfInput; i++) {
                preActivation_ += W[j][i] * x[i];
            }
            preActivation_ += b[j];

            y[j] = sigmoid(preActivation_);
        }
        return y;
    }

    //  전방향
    public double[] forward(double[] x) {
        return output(x);
    }

    //  후방향
    public double[][] backward(double[][] X, double[][] Z, double[][] dY, double[][] Wprev, int size, double eta) {
        double[][] dZ = new double[size][numOfOut];  // 오류 역전파
        double[][] grad_W = new double[numOfOut][numOfInput];
        double[] grad_b = new double[numOfOut];

        // 오류 역전파 오차 계산
        for (int n = 0; n < size; n++) {
            for (int j = 0; j < numOfOut; j++) {
                for (int k = 0; k < dY[0].length; k++) {  // 이전 학습 패턴과 같은 크기로 계산
                    dZ[n][j] += Wprev[k][j] * dY[n][k];
                }
                dZ[n][j] *= dsigmoid(Z[n][j]);

                for (int i = 0; i < numOfInput; i++) {
                    grad_W[j][i] += dZ[n][j] * X[n][i];
                }
                grad_b[j] += dZ[n][j];
            }
        }

        // 가중치 수정
        for (int j = 0; j < numOfOut; j++) {
            for (int i = 0; i < numOfInput; i++) {
                W[j][i] -= eta * grad_W[j][i] / size;
            }
            b[j] -= eta * grad_b[j] / size;
        }
        return dZ;
    }

    private double uniform(double min, double max, Random rng) {
        return rng.nextDouble() * (max - min) + min;
    }
}
