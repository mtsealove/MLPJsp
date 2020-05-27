package Utils;

import java.util.Random;

public class MLP {
    public int numOfInput; // 입력층 수
    public int numOfHidden; // 은닉층 수
    public int numOfOut; // 출력층 수
    public HiddenLayer hiddenLayer;
    public LogisticRegression logisticLayer;
    public Random rng;

    public MLP(int numOfInput, int numOfHidden, int numOfOut, Random rng) {
        this.numOfInput = numOfInput;
        this.numOfHidden = numOfHidden;
        this.numOfOut = numOfOut;

        if (rng == null) rng = new Random(1234);
        this.rng = rng;

        // 입력층 -> 은닉층 가중치 연산 객체 생성
        hiddenLayer = new HiddenLayer(numOfInput, numOfHidden, null, null, rng);  // sigmoid or tanh

        // 은닉층 -> 출력층 가중치 연산 객체 생성
        logisticLayer = new LogisticRegression(numOfHidden, numOfOut);
    }

    // 학습
    public boolean train(double[][] X, int T[][], int size, double eta) {
        double[][] prevW = hiddenLayer.W; // 학습 전의 가중치 확인
        double[][] Z = new double[size][numOfInput];
        double[][] dY;

        // 전방향 학습
        for (int n = 0; n < size; n++) {
            Z[n] = hiddenLayer.forward(X[n]);  // 활성함수 적용
        }
        // 출력층 전/후방향 학습
        dY = logisticLayer.train(Z, T, size, eta);
        // 후방향 학습
        hiddenLayer.backward(X, Z, dY, logisticLayer.W, size, eta);

        return isEnd(prevW, hiddenLayer.W);
    }

    // 이전 가중치와의 변화 비교
    private boolean isEnd(double[][] a, double[][] b) {
        double result = 0;

        // 이전 가중치의 모든 값이 차이의 합이 0.001 미만이면 탈출 신호
        if (result < 0.00000000001)
            return true;
        else
            return false;
    }

    // 예측
    public Integer[] predict(double[] x) {
        double[] z = hiddenLayer.output(x);
        return logisticLayer.predict(z);
    }
}
