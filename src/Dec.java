public class Dec {
    private static byte[][] Rcon = {{0x00, 0x00, 0x00, 0x00},
            {0x01, 0x00, 0x00, 0x00},
            {0x02, 0x00, 0x00, 0x00},
            {0x04, 0x00, 0x00, 0x00},
            {0x08, 0x00, 0x00, 0x00},
            {0x10, 0x00, 0x00, 0x00},
            {0x20, 0x00, 0x00, 0x00},
            {0x40, 0x00, 0x00, 0x00},
            {(byte) 0x80, 0x00, 0x00, 0x00},
            {(byte) 0x1b, 0x00, 0x00, 0x00},
            {(byte) 0x36, 0x00, 0x00, 0x00}};

    int Nb;
    int Nk;
    int Nr;
    byte[][] w;
    int[] inv_s_box_one_dime;
    Box sboxgenerator;

    //constructor with no parameter.
    public Dec(byte[][] w) {
        // TODO Auto-generated constructor stub
        Nb = 4;
        Nk = 4;
        Nr = 10;
        this.w = w;
        sboxgenerator = new Box();
        inv_s_box_one_dime = sboxgenerator.generate_inverse_one_dime();
    }

    public String statetostring(byte[][] state) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                stringBuilder.append(String.format("%02X", state[j][i]));
            }
        }
        String string = stringBuilder.toString();
        return string;
    }

    public String decryptstate(byte[][] state) {
        AddRoundKey(Nr, state);
        for (int i = Nr - 1; i > 0; i--) {
            state = InvShiftRows(state);
            InvSubBytes(state);
            AddRoundKey(i, state);
            state = InvMixColumns(state);
        }
        state = InvShiftRows(state);
        InvSubBytes(state);
        AddRoundKey(0, state);

        System.out.println("The decryption result:");
        System.out.println(statetostring(state));
        return statetostring(state);
    }

    public void InvSubBytes(byte[][] state) {
        for (int i = 0; i < Nb; i++) {
            for (int j = 0; j < Nb; j++) {
                state[i][j] = (byte) (inv_s_box_one_dime[state[i][j] & 0x00ff] & 0x00ff);
            }
        }
    }

    public byte[][] InvShiftRows(byte[][] state) {
        byte[][] tmp = new byte[Nb][Nb];
        for (int i = 0; i < Nb; i++) {
            for (int j = 0; j < Nb; j++) {
                tmp[i][j] = state[i][(Nb - i + j) % Nb];
            }
        }
        return tmp;
    }

    public byte[][] InvMixColumns(byte[][] state) {
        byte[] a = {0x0e, 0x0b, 0x0d, 0x09};

        byte[][] tmp = new byte[Nb][Nb];
        for (int j = 0; j < Nb; j++) {
            tmp[0][j] = (byte) (sboxgenerator.mul(a[0], state[0][j]) ^ sboxgenerator.mul(a[1], state[1][j]) ^
                    sboxgenerator.mul(a[2], state[2][j]) ^ sboxgenerator.mul(a[3], state[3][j]));
            tmp[1][j] = (byte) (sboxgenerator.mul(a[3], state[0][j]) ^ sboxgenerator.mul(a[0], state[1][j]) ^
                    sboxgenerator.mul(a[1], state[2][j]) ^ sboxgenerator.mul(a[2], state[3][j]));
            tmp[2][j] = (byte) (sboxgenerator.mul(a[2], state[0][j]) ^ sboxgenerator.mul(a[3], state[1][j]) ^
                    sboxgenerator.mul(a[0], state[2][j]) ^ sboxgenerator.mul(a[1], state[3][j]));
            tmp[3][j] = (byte) (sboxgenerator.mul(a[1], state[0][j]) ^ sboxgenerator.mul(a[2], state[1][j]) ^
                    sboxgenerator.mul(a[3], state[2][j]) ^ sboxgenerator.mul(a[0], state[3][j]));
        }
        return tmp;
    }

    public void AddRoundKey(int currentroundnum, byte[][] state) {
        for (int i = 0; i < 4; i++) {
            state[0][i] = (byte) (state[0][i] ^ w[Nb * currentroundnum + i][0]);
            state[1][i] = (byte) (state[1][i] ^ w[Nb * currentroundnum + i][1]);
            state[2][i] = (byte) (state[2][i] ^ w[Nb * currentroundnum + i][2]);
            state[3][i] = (byte) (state[3][i] ^ w[Nb * currentroundnum + i][3]);
        }
    }
}
