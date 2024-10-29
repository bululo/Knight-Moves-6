import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class MatrixExplorerThread {
    int[][] matrix;
    boolean[][] visited;
    int targetX;
    int targetY;
    String path = "";
    boolean valid;
    // movimentos possíveis
    int[] rowMove = {2, 2, -2, -2, 1, -1, 1, -1};
    int[] colMove = {1, -1, 1, -1, 2, 2, -2, -2};
    // string para saída do caminho encontrado
    String column = "abcdef";
    String row= "654321";

    public MatrixExplorerThread(int a, int b, int c, int targetX, int targetY) {
        this.matrix = new int[][]{
                {a, b, b, c, c, c},
                {a, b, b, c, c, c},
                {a, a, b, b, c, c},
                {a, a, b, b, c, c},
                {a, a, a, b, b, c},
                {a, a, a, b, b, c},
        };
        this.visited = new boolean[6][6];
        this.targetX = targetX;
        this.targetY = targetY;
        this.valid = false;
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        AtomicInteger smallestSumAtomic = new AtomicInteger();
        smallestSumAtomic.set(50);
        AtomicReference<String> smallestPathAtomic = new AtomicReference<>("");
        //
        int maxThreads = Runtime.getRuntime().availableProcessors();
        Semaphore semaphore = new Semaphore(maxThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(maxThreads);
        for (int a = 1; a < smallestSumAtomic.get(); a++) {
            for (int b = 1; b < smallestSumAtomic.get(); b++) {
                for (int c = 1; c < smallestSumAtomic.get(); c++) {
                    // Critérios de ABC para resposta válida
                    if ((a + b + c) < smallestSumAtomic.get() && a != b && a != c && b != c) {
                        int finalA = a;
                        int finalB = b;
                        int finalC = c;
                        // Limita o número de threads ativas
                        try {
                            semaphore.acquire();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        // Avalia a soma antes de lançar a thread
                        if ((a + b + c) < smallestSumAtomic.get())
                            executorService.submit(() -> {
                                // Caminho#1  a1 -> f6
                                MatrixExplorerThread a1tof6 = new MatrixExplorerThread(finalA, finalB, finalC, 0, 5);
                                a1tof6.explorePaths(a1tof6.matrix, 5, 0, a1tof6.visited, "a1", finalA, smallestSumAtomic);
                                if (a1tof6.valid) {
                                    // Caminho#2 a6 -> f1
                                    MatrixExplorerThread a6tof1 = new MatrixExplorerThread(finalA, finalB, finalC, 5, 5);
                                    a6tof1.explorePaths(a6tof1.matrix, 0, 0, a6tof1.visited, "a6", finalA, smallestSumAtomic);
                                    // Caso os dois caminhos sejam válidos
                                    if (a1tof6.valid && a6tof1.valid) {
                                        String path = finalA + "," + finalB + "," + finalC + "," + a1tof6.path + "," + a6tof1.path;
                                        System.out.println("Valid: " + path);
                                        if (finalA + finalB + finalC < smallestSumAtomic.get())
                                            smallestPathAtomic.set(path);
                                        else if (path.length() < smallestPathAtomic.get().length())
                                            smallestPathAtomic.set(path);
                                        smallestSumAtomic.set(finalA + finalB + finalC);
                                        System.out.println("Smallest Sum: " + smallestSumAtomic.get());
                                    }
                                }
                                semaphore.release();
                            });

                    }
                }
            }
        }
        // Espera threads terminarem
        executorService.shutdown();
        while (!executorService.isTerminated()) {

        }
        // Exibe o menor caminho
        System.out.println(smallestPathAtomic.get());
        // Exibe o tempo decorrido
        long elapsedTimeMillis = System.currentTimeMillis() - start;
        float elapsedTimeSec = elapsedTimeMillis / 1000F;
        System.out.println("Elasped time: " + elapsedTimeSec + " seconds");
    }
    // Verifica se uma posição está dentro dos limites da matriz e não visitada
    boolean isSafe(int x, int y, boolean[][] visited) {
        return (x >= 0 && x < 6 && y >= 0 && y < 6 && !visited[x][y]);
    }

    // Função recursiva que explora todos os caminhos a partir da posição (x, y)
    void explorePaths(int[][] matrix, int x, int y, boolean[][] visited, String path, int score, AtomicInteger smallestSumAtomic) {
        // Se chega a casa alvo com a pontuação alvo
        if (x == targetX && y == targetY && score == 2024 && !valid) {
                this.path = path;
                valid = true;
            return;
        }
        // Critérios de parada da busca: score estourado, caminho já encontrado pra combinação de ABC ou ABC>ABC' válido
        if (score > 2024 || valid || (matrix[0][0] + matrix[1][1] + matrix[5][5]) >= smallestSumAtomic.get()) {
            return;
        }

        visited[x][y] = true;
        // explora os movimentos válidos
        for (int i = 0; i < 8; i++) {
            int newRow = x + rowMove[i];
            int newCol = y + colMove[i];
            int newScore;

            if (isSafe(newRow, newCol, visited)) {
                if (matrix[x][y] == matrix[newRow][newCol]) {
                    newScore = score + matrix[newRow][newCol];
                } else {
                    newScore = score * matrix[newRow][newCol];
                }
                explorePaths(matrix, newRow, newCol, visited, path + "," + column.charAt(newCol) + row.charAt(newRow), newScore, smallestSumAtomic);
            }
        }
        // backtracking
        visited[x][y] = false;
    }
}
