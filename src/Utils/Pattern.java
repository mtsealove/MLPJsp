package Utils;

// 학습 패턴 클래스
public class Pattern {
    int[] P;

    public Pattern(String line) {
        P = setPattern(line);
    }


    public int[] getP() {
        return P;
    }

    public void setP(int[] p) {
        P = p;
    }

    //  문자열을 읽어 학습 1차원 배열로 변환
    private int[] setPattern(String line) {
        int[] p = new int[25];
        String[] words = line.split(",");
        for (int i = 0; i < 25; i++) {
            int num = Integer.parseInt(words[i]);
            p[i] = num;
        }
        return p;
    }

}
