/**
 * John Meyer
 * CSE 271
 * Dr. Angel Bravo
 * Lab03
 */
public class Student {

    // Define instance variables
    private String name;
    private int totalScore;
    private int totalNumberOfQuizes;

    /**
     * Gets name
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets total score
     * @return The total score
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Gets total number of quizes
     * @return The total number of quizzes
     */
    public int getTotalNumberOfQuizes() {
        return totalNumberOfQuizes;
    }

    /**
     * Sets name
     * @param name The name; must be longer than 0
     */
    public void setName(String name) {
        if (name.length() > 0) {
            this.name = name;
        }
    }

    /**
     * Sets total score
     * @param totalScore The total score; must be in the interval [0, 100]
     */
    public void setTotalScore(int totalScore) {
        if (totalScore >= 0 && totalScore <= 100) {
            this.totalScore = totalScore;
        }
    }

    /**
     * Sets the total number of quizzes
     * @param totalNumberOfQuizes The number of quizzes; must be greater than or eqaul to 0
     */
    public void setTotalNumberOfQuizes(int totalNumberOfQuizes) {
        if (totalNumberOfQuizes >= 0) {
            this.totalNumberOfQuizes = totalNumberOfQuizes;
        }
    }

    /**
     * Adds a quiz score
     * @param score The quiz score; must be in the interval [0, 100]
     */
    public void addQuiz(int score) {
        if (score > 0 && score < 100) {
            totalScore += score;
            totalNumberOfQuizes++;
        }
    }

    /**
     * Computes the average score
     * @return The average score
     */
    public double getAverageScore() {
        return (1.0 * totalScore / totalNumberOfQuizes);
    }

    // Define constructors
    public Student(String name) {
        this.name = name;
        this.totalScore = 0;
        this.totalNumberOfQuizes = 0;
    }

    public Student(String name, int totalScore, int totalNumberOfQuizes) {
        this.name = name;
        this.totalScore = totalScore;
        this.totalNumberOfQuizes = totalNumberOfQuizes;
    }
}
