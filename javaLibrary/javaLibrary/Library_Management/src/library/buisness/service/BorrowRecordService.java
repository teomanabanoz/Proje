package library.buisness.service;

import library.domain.BorrowRecord;
import library.repository.BorrowRecordRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BorrowRecordService implements IBorrowRecordService {
    private final BorrowRecordRepository borrowRecordRepository;

    public BorrowRecordService(BorrowRecordRepository borrowRecordRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
    }

    @Override
    public void addBorrowRecord(BorrowRecord borrowRecord) {
        validateBorrowRecord(borrowRecord);
        validateBookAvailability(borrowRecord);
        borrowRecordRepository.add(borrowRecord);
    }

    @Override
    public void updateBorrowRecord(BorrowRecord borrowRecord) {
        if (borrowRecord == null) {
            throw new IllegalArgumentException("Borrow record cannot be null");
        }
        borrowRecordRepository.update(borrowRecord);
    }

    @Override
    public void deleteBorrowRecord(int id) {
        borrowRecordRepository.delete(id);
    }

    @Override
    public BorrowRecord getBorrowRecordById(int id) {
        Optional<BorrowRecord> record = borrowRecordRepository.getById(id);
        return record.orElseThrow(() -> new RuntimeException("Borrow record not found with id: " + id));
    }

    @Override
    public Map<Integer, BorrowRecord> getAllBorrowRecords() {
        Optional<Map<Integer, BorrowRecord>> records = borrowRecordRepository.getAll();
        return records.orElseThrow(() -> new RuntimeException("Unable to retrieve borrow records"));
    }

    @Override
    public Map<Integer, BorrowRecord> getActiveBorrows() {
        return borrowRecordRepository.findActiveBorrows();
    }

    @Override
    public Map<Integer, BorrowRecord> getOverdueBorrows() {
        return borrowRecordRepository.findOverdueBorrows();
    }

    @Override
    public List<BorrowRecord> getBorrowRecordsByMemberId(int memberId) {
        return borrowRecordRepository.findBorrowRecordsByMemberId(memberId);
    }

    @Override
    public List<BorrowRecord> getActiveBorrowRecordsByMemberId(int memberId) {
        return borrowRecordRepository.findActiveBorrowRecordsByMemberId(memberId);
    }

    @Override
    public BorrowRecord getActiveBookBorrow(int bookId) {
        Optional<BorrowRecord> record = borrowRecordRepository.findActiveBookBorrow(bookId);
        return record.orElse(null);
    }

    @Override
    public List<BorrowRecord> getBorrowsByBookId(int bookId) {
        return borrowRecordRepository.findByBookId(bookId);
    }

    @Override
    public List<BorrowRecord> getBorrowsByDateRange(LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        return borrowRecordRepository.findBorrowsByDateRange(startDate, endDate);
    }

    @Override
    public int getActiveBorrowsCount() {
        return borrowRecordRepository.activeBorrowsCount();
    }

    @Override
    public int getOverdueBorrowsCount() {
        return borrowRecordRepository.overdueBorrowsCount();
    }

    private void validateBorrowRecord(BorrowRecord borrowRecord) {
        if (borrowRecord == null) {
            throw new IllegalArgumentException("Borrow record cannot be null");
        }
        if (borrowRecord.getBook() == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if (borrowRecord.getMember() == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }
        if (borrowRecord.getBorrowDate() == null) {
            throw new IllegalArgumentException("Borrow date cannot be null");
        }
        if (borrowRecord.getDueDate() == null) {
            throw new IllegalArgumentException("Due date cannot be null");
        }
        if (borrowRecord.getDueDate().isBefore(borrowRecord.getBorrowDate())) {
            throw new IllegalArgumentException("Due date cannot be before borrow date");
        }
    }

    private void validateBookAvailability(BorrowRecord borrowRecord) {
        Optional<BorrowRecord> activeBookBorrow = borrowRecordRepository.findActiveBookBorrow(borrowRecord.getBook().getId());
        if (activeBookBorrow.isPresent()) {
            throw new IllegalStateException("Book is already borrowed");
        }
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
    }
}
