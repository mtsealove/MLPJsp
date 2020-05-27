package Utils;//import static util.ActivationFunction.softmax;

public class LogisticRegression {

    // 입력층 -> 은닉층 기준
    public int numOfIn; // 입력 노드 수
    public int numOfOut; // 출력 노드 수
    public double[][] W; // 가중치
    public double[] b; //


    public LogisticRegression(int numOfIn, int numOfOut) {
        this.numOfIn = numOfIn;
        this.numOfOut = numOfOut;
        W = new double[numOfOut][numOfIn]; // 입출력 행렬의 가중치 생성
        b = new double[numOfOut];
    }

    // 학습 진행
    public double[][] train(double[][] X, int T[][], int size, double eta) {
        double[][] grad_W = new double[numOfOut][numOfIn];
        double[] grad_b = new double[numOfOut];
        double[][] dY = new double[size][numOfOut];

        // 가중치 계산
        for (int n = 0; n < size; n++) {
            double[] predicted_Y_ = output(X[n]);
            for (int j = 0; j < numOfOut; j++) {
                dY[n][j] = predicted_Y_[j] - T[n][j];
                for (int i = 0; i < numOfIn; i++) {
                    grad_W[j][i] += dY[n][j] * X[n][i];
                }
                grad_b[j] += dY[n][j];
            }
        }
        // 가중치 변경
        for (int j = 0; j < numOfOut; j++) {
            for (int i = 0; i < numOfIn; i++) {
                W[j][i] -= eta * grad_W[j][i] / size;
            }
            b[j] -= eta * grad_b[j] / size;
        }

        return dY;
    }

    private double[] output(double[] x) {
        double[] preActivation = new double[numOfOut];

        for (int j = 0; j < numOfOut; j++) {
            for (int i = 0; i < numOfIn; i++) {
                preActivation[j] += W[j][i] * x[i];
            }
            preActivation[j] += b[j];
        }
        return preActivation;
    }


//  예측
    public Integer[] predict(double[] x) {
        double[] y = output(x);  // 활성함수를 통해 출력값 가져옴
        Integer[] t = new Integer[numOfOut];

        int argmax = -1;
        double max = 0.;

        for (int i = 0; i < numOfOut; i++) {
            if (max < y[i]) {
                max = y[i];
                argmax = i;
            }
        }
        for (int i = 0; i < numOfOut; i++) {
            if (i == argmax) {
                t[i] = 1;
            } else {
                t[i] = 0;
            }
        }
        return t;
    }
}
