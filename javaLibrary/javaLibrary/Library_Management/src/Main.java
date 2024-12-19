import javax.swing.*;

import library.buisness.service.*;
import library.presentation.MainScreen;
import library.repository.BookRepository;
import library.repository.BorrowRecordRepository;
import library.repository.MemberRepository;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                BookRepository bookRepository = new BookRepository();
                IBookService bookService = new BookService(bookRepository);
                MemberRepository memberRepository = new MemberRepository();
                IMemberService memberService = new MemberService(new MemberRepository());
                BorrowRecordRepository borrowRecordRepository = new BorrowRecordRepository();
                IBorrowRecordService borrowRecordService = new BorrowRecordService(new BorrowRecordRepository());

                new MainScreen(bookService, memberService, borrowRecordService);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}