import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Instructions extends JFrame {
    public Instructions() {
        setTitle("Інструкція до гри Морський бій");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTextArea textArea = new JTextArea();
        textArea.setText(
                "Загальні правила:\n" +
                        "1. Мета гри: Потопити всі кораблі супротивника, розташовані на його ігровому полі.\n" +
                        "2. Ігрове поле: Ігрове поле складається з сітки залежно від рівня складності.\n" +
                        "3. Розташування кораблів: На початку гри кожен гравець розміщує свої кораблі на своєму ігровому полі.\n" +
                        "4. Типи кораблів:\n" +
                        "   - 1 корабель довжиною 4 клітинки (лінкор).\n" +
                        "   - 2 кораблі довжиною 3 клітинки (крейсери).\n" +
                        "   - 3 кораблі довжиною 2 клітинки (есмінці).\n" +
                        "   - 4 кораблі довжиною 1 клітинка (підводні човни).\n" +
                        "5. Хід гри: Гравці по черзі стріляють по клітинках ігрового поля супротивника, намагаючись влучити в кораблі.\n" +
                        "6. Результат пострілу: Якщо постріл влучив у корабель, клітинка позначається, і гравець може продовжити стріляти. Якщо не влучив, хід передається супротивнику.\n" +
                        "7. Перемога: Переможцем стає той гравець, який першим потопить всі кораблі супротивника.\n\n" +
                        "Рівні складності:\n" +
                        "1. Легкий рівень:\n" +
                        "   - Ігрове поле 10x10.\n" +
                        "   - 5 кораблів.\n" +
                        "   - Супротивник розміщує кораблі випадково.\n" +
                        "   - Супротивник стріляє по випадкових клітинках.\n\n" +
                        "2. Середній рівень:\n" +
                        "   - Ігрове поле 13x13.\n" +
                        "   - 7 кораблів.\n" +
                        "   - Кількість пострілів обмежена.\n" +
                        "   - Супротивник розміщує кораблі з певною стратегією, наприклад, не розміщує кораблі поряд один з одним.\n" +
                        "   - Супротивник стріляє більш осмислено, з урахуванням попередніх влучень.\n\n" +
                        "3. Важкий рівень:\n" +
                        "   - Ігрове поле 15x15.\n" +
                        "   - 10 кораблів.\n" +
                        "   - Супротивник розміщує кораблі за оптимальною стратегією, використовуючи найкращі тактичні розміщення.\n" +
                        "   - Супротивник стріляє з максимальною ефективністю, аналізуючи кожен свій і ваш хід.\n"
        );
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Закрити");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        add(closeButton, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Instructions().setVisible(true);
            }
        });
    }
}
