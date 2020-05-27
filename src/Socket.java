import Utils.Db;
import Utils.MLP;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

@ServerEndpoint("/Compare")
public class Socket {
    double[][] test_X;
    private static Set<Session> clients = Collections
            .synchronizedSet(new HashSet<Session>());

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
//      소켓으로 받은 데이터를 행렬로 변환
        test_X = new double[1][25];
        String[] line = message.split(",");
        for (int i = 0; i < line.length; i++) {
            double num = Double.parseDouble(line[i]);
            if (num == -1) {
                num = 0;
            }
            test_X[0][i] = num;
        }
        final Random rng = new Random(123);  // 초기 가중치의 랜덤 생성을 위해 이용
        Db db = new Db();

        final int train_N = db.getRow(); // 학습 패턴 수
        final int test_N = 1; //
        final int numOfInput = 25; // 입력층 수
        final int numOfHidden = 7; // 은닉층 수
        final int numOfOut = 10; // 출력층 수


        double[][] train_X = db.getInput(); // 학습 입력층
        int[][] train_T = db.getOutput(); // 학습 출력층
        for (double[] train : train_X) {
            System.out.println(Arrays.toString(train));
        }

        Integer[][] predicted_T = new Integer[test_N][numOfOut];

        final int epochs = 150000;
        double eta = 0.1;

        final int minibatchSize = 1;  // 학습 회수 , 각 epoch 별로 1회씩 진행합니다.
        int minibatch_N = train_N / minibatchSize;

        double[][][] train_X_batch = new double[minibatch_N][minibatchSize][numOfInput];
        int[][][] train_T_batch = new int[minibatch_N][minibatchSize][numOfOut];
        List<Integer> batchIdx = new ArrayList<>();
        for (int i = 0; i < train_N; i++) batchIdx.add(i);
        Collections.shuffle(batchIdx, rng);

        // 층간 배치 임의 설정
        for (int i = 0; i < minibatch_N; i++) {
            for (int j = 0; j < minibatchSize; j++) {
                train_X_batch[i][j] = train_X[batchIdx.get(i * minibatchSize + j)];
                train_T_batch[i][j] = train_T[batchIdx.get(i * minibatchSize + j)];
            }
        }

        // 연산 객체 생성
        MLP mlp = new MLP(numOfInput, numOfHidden, numOfOut, rng);
        // 각 입출력층 학습 진행
        boolean complete = false; // 2차원 반복을 탈출하기 위해 사용
        for (int epoch = 0; epoch < epochs; epoch++) {
            for (int batch = 0; batch < minibatch_N; batch++) {
                if (mlp.train(train_X_batch[batch], train_T_batch[batch], minibatchSize, eta)) {
//                    complete = true;
                    break;
                }
            }
            if (complete) {
                System.out.println("epoch: " + epoch);
                break;
            }
            // epoch 송신
            synchronized (clients) {
                for (Session client : clients) {
                    client.getBasicRemote().sendText("epoch: " + epoch);
                }
                System.out.println("epoch: " + epoch);
            }
        }
        // 예측
        for (int i = 0; i < test_N; i++) {
            predicted_T[i] = mlp.predict(test_X[i]);
        }

        int[] predict = new int[numOfOut];
        for (int i = 0; i < numOfOut; i++) {
            predict[i] = predicted_T[0][i];
        }
        for (Integer[] p : predicted_T) {
            System.out.println(Arrays.toString(p));
        }

        System.out.println("original");
        System.out.println(Arrays.toString(test_X[0]));

        System.out.println("----------------------");
        for (int i = 0; i < train_T.length; i++) {
            if (Arrays.equals(train_T[i], predict)) {
                System.out.println("result");
                System.out.println(Arrays.toString(train_X[i]));
                // 학습 결과 송신
                synchronized (clients) {
                    for (Session client : clients) {
                        client.getBasicRemote().sendText("result");
                        client.getBasicRemote().sendText(Arrays.toString(train_X[i]));
                    }
                }
                break;
            }
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        clients.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        clients.remove(session);
    }
}
