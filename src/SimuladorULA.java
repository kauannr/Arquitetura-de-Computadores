
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class SimuladorULA {

    public static void main(String[] args) {
        String caminhoEntrada = "C:\\Users\\kauan\\Downloads\\Faculdade\\Projeto\\projeto\\instrucoes.txt";
        String caminhoSaida = "C:\\Users\\kauan\\Downloads\\Faculdade\\Projeto\\projeto\\log.txt";
        int PC = 0; // Contador de Programa

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoEntrada));
                PrintWriter pw = new PrintWriter(new FileWriter(caminhoSaida))) {
            String line;

            StringBuilder Hvalor = new StringBuilder("00000000000000000000000000000001");
            StringBuilder OPCvalor = new StringBuilder("00000000000000000000000000000000");
            StringBuilder TOSvalor = new StringBuilder("00000000000000000000000000000010");
            StringBuilder CPPvalor = new StringBuilder("00000000000000000000000000000000");
            StringBuilder LVvalor = new StringBuilder("00000000000000000000000000000000");
            StringBuilder SPvalor = new StringBuilder("00000000000000000000000000000100");
            StringBuilder PCvalor = new StringBuilder("00000000000000000000000000000000");
            StringBuilder MDRvalor = new StringBuilder("00000000000000000000000000000000");
            StringBuilder MARvalor = new StringBuilder("00000000000000000000000000000100");
            String MBRvalor = "00000000";
            String MBRUvalor = "00000000";
            String A_binario;
            String B_binario = "";
            A_binario = Hvalor.toString();

            StringBuilder stringComEspaco = new StringBuilder();
            while ((line = br.readLine()) != null) {
                Hvalor.setLength(0);

                for (int i = 0; i < line.length(); i++) {
                    stringComEspaco.append(line.charAt(i));
                    if (i < line.length() - 1) {
                        // Evita adicionar um espaço após o último caractere
                        stringComEspaco.append(" ");
                    }
                }

                String IR = stringComEspaco.toString(); // IR é a Instrução atual
                stringComEspaco.setLength(0);
                String[] tokens = IR.split(" ");

                int SLL8 = Integer.parseInt(tokens[0]);
                int SRA1 = Integer.parseInt(tokens[1]);
                int F0 = Integer.parseInt(tokens[2]);
                int F1 = Integer.parseInt(tokens[3]);
                int ENA = Integer.parseInt(tokens[4]);
                int ENB = Integer.parseInt(tokens[5]);
                int INVA = Integer.parseInt(tokens[6]);
                int INC = Integer.parseInt(tokens[7]);
                int H = Integer.parseInt(tokens[8]);
                int OPC = Integer.parseInt(tokens[9]);
                int TOS = Integer.parseInt(tokens[10]);
                int CPP = Integer.parseInt(tokens[11]);
                int LV = Integer.parseInt(tokens[12]);
                int SP = Integer.parseInt(tokens[13]);
                int PC2 = Integer.parseInt(tokens[14]);
                int MDR = Integer.parseInt(tokens[15]);
                int MAR = Integer.parseInt(tokens[16]);
                int bit1Memoria = Integer.parseInt(tokens[17]);
                int bit2Memoria = Integer.parseInt(tokens[18]);
                int bit1BarramentoB = Integer.parseInt(tokens[19]);
                int bit2BarramentoB = Integer.parseInt(tokens[20]);
                int bit3BarramentoB = Integer.parseInt(tokens[21]);
                int bit4BarramentoB = Integer.parseInt(tokens[22]);

                // procurando o registrador que comanda B:
                String decodificadorString = Integer.toString(bit1BarramentoB) + Integer.toString(bit2BarramentoB)
                        + Integer.toString(bit3BarramentoB) + Integer.toString(bit4BarramentoB);

                System.out.println(decodificadorString);
                int decodificadorInt = Integer.parseInt(decodificadorString, 2);
                System.out.println(decodificadorInt);

                if (decodificadorInt == 0) {
                    B_binario = MDRvalor.toString();
                } else if (decodificadorInt == 1) {
                    B_binario = PCvalor.toString();
                } else if (decodificadorInt == 2) {
                    char bitDeSinal = MBRvalor.charAt(0); // Pega o bit de sinal
                    String preench = new String(new char[24]).replace("\0", String.valueOf(bitDeSinal));
                    // Cria um preenchimento de 24 bits usando o bit de sinal
                    B_binario = preench + MBRvalor; // Concatena o preench com o valor de MBR para obter 32 bits
                } else if (decodificadorInt == 3) {
                    String preenchimento = "000000000000000000000000"; // preenchimento de 24 zeros
                    // Concatena o preenchimento com o valor de MBRU para obter 32 bits:
                    B_binario = preenchimento + MBRUvalor;
                } else if (decodificadorInt == 4) {
                    B_binario = SPvalor.toString();
                } else if (decodificadorInt == 5) {
                    B_binario = LVvalor.toString();
                } else if (decodificadorInt == 6) {
                    B_binario = CPPvalor.toString();
                } else if (decodificadorInt == 7) {
                    B_binario = TOSvalor.toString();
                } else if (decodificadorInt == 8) {
                    B_binario = OPCvalor.toString();
                }

                // A e B para decimal:
                BigInteger auxA = new BigInteger(A_binario, 2);
                int A_decimal = auxA.intValue();
                BigInteger auxB = new BigInteger(B_binario, 2);
                int B_decimal = auxB.intValue();

                int S = 0;
                int Vaium = 0;
                // INVA para inverter a entrada de A
                if (INVA == 1) {
                    A_decimal = ~A_decimal;
                }

                // Verificar se A e B estão habilitados
                A_decimal = ENA == 1 ? A_decimal : 0;
                B_decimal = ENB == 1 ? B_decimal : 0;

                // Realizar operação de acordo com F0 e F1
                if (F0 == 0 && F1 == 0) {
                    // A AND B
                    S = A_decimal & B_decimal;
                } else if (F0 == 0 && F1 == 1) {
                    // A OR B
                    S = A_decimal | B_decimal;
                } else if (F0 == 1 && F1 == 0) {
                    // NOT B
                    S = ~B_decimal;
                } else if (F0 == 1 && F1 == 1) {
                    // A + B
                    try {
                        S = Math.addExact(A_decimal, B_decimal);
                        if (INC == 1) {
                            S = Math.addExact(S, 1); // Adiciona 1 ao resultado
                        }
                    } catch (ArithmeticException e) {
                        Vaium = 1;
                    }
                }

                int Z = S == 0 ? 1 : 0;

                if (SLL8 == 1) {
                    S <<= 8; // Desloca 8 bits para a esquerda
                }
                if (SRA1 == 1) {
                    int sinal = S & 0x80000000; // Captura o bit de sinal
                    S >>= 1; // Desloca para a direita
                    S &= 0x3FFFFFFF; // Limpa o bit de sinal para garantir que seja 0
                    S |= sinal; // Restaura o bit de sinal

                }

                // StringBuilder
                StringBuilder escritaNoLog = new StringBuilder();
                escritaNoLog.append("\nMAR = " + MARvalor);
                escritaNoLog.append("\nMDR = " + MDRvalor);
                escritaNoLog.append("\nPC = " + PCvalor);
                escritaNoLog.append("\nSP = " + SPvalor);
                escritaNoLog.append("\nLV = " + LVvalor);
                escritaNoLog.append("\nCPP = " + CPPvalor);
                escritaNoLog.append("\nTOS = " + TOSvalor);
                escritaNoLog.append("\nOPC = " + OPCvalor);
                escritaNoLog.append("\nMBR = " + MBRvalor + "\n");
                escritaNoLog.append(
                        "IR: " + IR + ", A: " + decimalParaBinario(A_decimal) + ", B: " + decimalParaBinario(B_decimal)
                                + ", S: " + decimalParaBinario(S) + ", Vaium: " + Vaium + ", Z = " + Z);
                // Registradores:
                System.out.println(decimalParaBinario(S));
                // Definindo os registradores que vão ser habilitados:
                if (MAR > 0) {
                    MARvalor.setLength(0);
                    BigInteger auxiliar = new BigInteger(decimalParaBinario(S), 2);
                    MAR = auxiliar.intValue();
                    MARvalor.append(auxiliar);
                    String MAR32bit = String.format("%32s", Integer.toBinaryString(MAR)).replace(' ',
                            '0');
                    escritaNoLog.append(" MAR: " + MAR32bit);
                    MARvalor.setLength(0);
                    MARvalor.append(decimalParaBinario(MAR));
                }
                if (MDR > 0) {
                    MDRvalor.setLength(0);
                    BigInteger auxiliar = new BigInteger(decimalParaBinario(S), 2);
                    MDR = auxiliar.intValue();
                    String MDR32bit = String.format("%32s", Integer.toBinaryString(MDR)).replace(' ',
                            '0');
                    escritaNoLog.append(" MDR: " + MDR32bit);
                    MDRvalor.append(decimalParaBinario(MDR));
                }
                if (PC2 > 0) {
                    PCvalor.setLength(0);
                    BigInteger auxiliar = new BigInteger(decimalParaBinario(S), 2);
                    PC2 = auxiliar.intValue();
                    String PC232bit = String.format("%32s", Integer.toBinaryString(PC2)).replace(' ',
                            '0');
                    escritaNoLog.append(" PC: " + PC232bit);
                    PCvalor.append(decimalParaBinario(PC2));
                }
                if (SP > 0) {
                    SPvalor.setLength(0);
                    BigInteger auxiliar = new BigInteger(decimalParaBinario(S), 2);
                    SP = auxiliar.intValue();
                    String SP32bit = String.format("%32s", Integer.toBinaryString(SP)).replace(' ',
                            '0');
                    escritaNoLog.append(" SP: " + SP32bit);
                    SPvalor.append(decimalParaBinario(SP));
                }
                if (LV > 0) {
                    LVvalor.setLength(0);
                    BigInteger auxiliar = new BigInteger(decimalParaBinario(S), 2);
                    LV = auxiliar.intValue();
                    String LV32bit = String.format("%32s", Integer.toBinaryString(LV)).replace(' ',
                            '0');
                    escritaNoLog.append(" LV: " + LV32bit);
                    LVvalor.append(decimalParaBinario(LV));
                }
                if (CPP > 0) {
                    CPPvalor.setLength(0);
                    BigInteger auxiliar = new BigInteger(decimalParaBinario(S));
                    CPP = auxiliar.intValue();
                    String CPP32bit = String.format("%32s", Integer.toBinaryString(CPP)).replace(' ',
                            '0');
                    escritaNoLog.append(" CPP: " + CPP32bit);
                    CPPvalor.append(decimalParaBinario(CPP));
                }
                if (TOS > 0) {
                    TOSvalor.setLength(0);

                    BigInteger auxiliar = new BigInteger(decimalParaBinario(S), 2);
                    TOS = auxiliar.intValue();
                    String TOS32bit = String.format("%32s", Integer.toBinaryString(TOS)).replace(' ',
                            '0');
                    escritaNoLog.append(" TOS: " + TOS32bit);
                    TOSvalor.append(decimalParaBinario(TOS));
                }
                if (OPC > 0) {
                    OPCvalor.setLength(0);
                    BigInteger auxiliar = new BigInteger(decimalParaBinario(S), 2);
                    OPC = auxiliar.intValue();
                    String OPC32bit = String.format("%32s", Integer.toBinaryString(OPC)).replace(' ',
                            '0');
                    escritaNoLog.append(" OPC: " + OPC32bit);
                    OPCvalor.append(decimalParaBinario(OPC));
                }
                if (H > 0) {
                    Hvalor.setLength(0);
                    BigInteger auxiliar = new BigInteger(decimalParaBinario(S), 2);
                    Hvalor.append(decimalParaBinario(S));

                    escritaNoLog.append(" H: " + Hvalor);
                    A_binario = Hvalor.toString();
                }

                // escrever resultado na saída
                escritaNoLog.append("\n");
                pw.println(escritaNoLog);
                PC++;

                // TAREFA 3 AQUI:
                String caminhoDados = "C:\\Users\\kauan\\Downloads\\Faculdade\\Projeto\\projeto\\dados.txt";
                try (BufferedReader br1 = new BufferedReader(new FileReader(caminhoDados))) {
                    BufferedReader br2 = new BufferedReader(new FileReader(caminhoDados));
                    String line1;
                    // LER TODO O ARQUIVO E ARMAZENAR NA LISTA:
                    List<String> listaComOConteudo = new ArrayList<>();

                    while ((line1 = br1.readLine()) != null) {
                        listaComOConteudo.add(line1);
                    }
                    String[] vectorLinhas = listaComOConteudo.toArray(new String[0]);
                    //

                    String line2;

                    if (bit1Memoria == 1) {
                        int posicaoApontadaPorMAR = Integer.parseInt(MARvalor.toString(), 2);
                        System.out.println(posicaoApontadaPorMAR);
                        listaComOConteudo.set(posicaoApontadaPorMAR, MDRvalor.toString());

                        escritaNoLog.setLength(0);
                        try (PrintWriter pw2 = new PrintWriter(new File(caminhoDados))) {
                            for (String string : listaComOConteudo) {
                                pw2.println(string);
                                escritaNoLog.append(string + "\n");
                            }
                            pw.println("\n" + escritaNoLog);

                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                    } else if (bit2Memoria == 1) {
                        int posicaoApontadaPorMAR = Integer.parseInt(MARvalor.toString(), 2);
                        System.out.println(posicaoApontadaPorMAR);
                        escritaNoLog.setLength(0);
                        for (String string : listaComOConteudo) {
                            escritaNoLog.append(string + "\n");
                        }
                        pw.println(escritaNoLog);
                        MDRvalor.setLength(0);
                        MDRvalor.append(listaComOConteudo.get(posicaoApontadaPorMAR));
                        int valorDaLinha = Integer.parseInt(listaComOConteudo.get(posicaoApontadaPorMAR), 2);
                        MDR = valorDaLinha;
                        String MDR32bit = String.format("%32s",
                                Integer.toBinaryString(MDR)).replace(' ',
                                        '0');
                        escritaNoLog.setLength(0);
                        escritaNoLog.append(" MDR: " + MDR32bit);
                        pw.println(escritaNoLog);

                    }
                    escritaNoLog.setLength(0);
                    escritaNoLog.append("-------------------------------");
                    pw.println(escritaNoLog);

                } catch (Exception e) {
                    System.out.println("Erro no caminho do dados.txt. " + e.getMessage());
                    e.printStackTrace();
                }

            }
        } catch (

        Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Função para converter uma string binária em um número decimal
    static int binarioParaDecimal(String binaryString) {
        if (binaryString.charAt(0) == '1') { // Se for um valor negativo em complemento de dois
            return (int) Long.parseLong(binaryString, 2);
        } else {
            return Integer.parseInt(binaryString, 2);
        }
    }

    // Função para converter um número decimal em uma string binária
    static String decimalParaBinario(int number) {
        return String.format("%32s", Integer.toBinaryString(number)).replace(' ', '0');
    }
}