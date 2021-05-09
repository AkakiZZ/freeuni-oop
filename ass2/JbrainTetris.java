import javax.swing.*;
import java.awt.*;

public class JBrainTetris extends JTetris{

    private JCheckBox brainMode;
    private JCheckBox animateMode;
    private JSlider adversarySlider;

    private int currentCount;

    private Brain brain;
    private Brain.Move best;

    /**
     * Creates a new JTetris where each tetris square
     * is drawn with the given number of pixels.
     *
     * @param pixels
     */
    JBrainTetris(int pixels) {
        super(pixels);
        brain = new DefaultBrain();
        currentCount = -1;
        best = null;
    }

    @Override
    public JComponent createControlPanel() {
        JPanel panel = (JPanel) super.createControlPanel();
        brainMode = new JCheckBox("Brain");
        panel.add(brainMode);

        animateMode = new JCheckBox("Animate");
        animateMode.setSelected(true);
        panel.add(animateMode);

        JPanel row = new JPanel();
        panel.add(row);

        adversarySlider = new JSlider(0, 100);
        adversarySlider.setValue(0);
        adversarySlider.setPreferredSize(new Dimension(100, 15));

        row.add(new JLabel("Adversary:"));
        row.add(adversarySlider);

        return panel;
    }

    @Override
    public void tick(int verb) {
        if (brainMode.isSelected() && verb == DOWN) {
            if(currentCount != count) {
                currentCount = count;
                board.undo();
                best = brain.bestMove(board, currentPiece, HEIGHT, best);
            }
            if (best != null) {
                if (!currentPiece.equals(best.piece)) super.tick(ROTATE);
                else if (best.x > currentX) super.tick(RIGHT);
                else if (best.x < currentX) super.tick(LEFT);
                else if (animateMode.isSelected() && best.y < currentY) super.tick(DROP);
            }
        }
        super.tick(verb);
    }

    @Override
    public Piece pickNextPiece() {
        int randomNum = (int)Math.floor(Math.random() * 99 + 1);
        if (randomNum > adversarySlider.getValue()) {
            return super.pickNextPiece();
        }
        Piece worstPiece = null;
        double worstScore = 0;
        Brain.Move move = null;
        for (Piece p : Piece.getPieces()) {
            if (worstPiece == null) worstPiece = p;
            move = brain.bestMove(board, p, HEIGHT, move);
            if (worstScore < move.score) {
                worstScore = move.score;
                worstPiece = p;
            }
        }
        return worstPiece;
    }

    public static void main(String[] args) {
        // Set GUI Look And Feel Boilerplate.
        // Do this incantation at the start of main() to tell Swing
        // to use the GUI LookAndFeel of the native platform. It's ok
        // to ignore the exception.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        JBrainTetris tetris = new JBrainTetris(16);

        JFrame frame = JTetris.createFrame(tetris);
        frame.setVisible(true);
    }
}
