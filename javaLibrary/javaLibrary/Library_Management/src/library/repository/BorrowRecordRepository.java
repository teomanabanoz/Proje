package library.repository;

import library.domain.BorrowRecord;
import library.repository.base.GenericRepository;
import library.storage.DataStorage;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class BorrowRecordRepository extends GenericRepository<BorrowRecord, Map<Integer, BorrowRecord>> implements IBorrowRecordRepository {

    @Override
    protected EntityType getEntityType() {
        return EntityType.BORROW_RECORD;
    }

    @Override
    public void delete(int id) {
        DataStorage.getBorrowRecords().remove(id);
    }

    @Override
    public void update(BorrowRecord entity) {
        entity.setReturnDate();
        DataStorage.getBorrowRecords().put(entity.getId(), entity);
    }

    @Override
    public Optional<Map<Integer, BorrowRecord>> getAll() {
        return Optional.of(new HashMap<Integer, BorrowRecord>(DataStorage.getBorrowRecords()));
    }

    @Override
    public Optional<BorrowRecord> getById(int id) {
        return DataStorage.getBorrowRecords().values()
                .stream().filter(borrowRecord -> borrowRecord.getId() == id)
                .findFirst();
    }

    @Override
    public Map<Integer, BorrowRecord> findActiveBorrows() {
        return DataStorage.getBorrowRecords().values()
                .stream().filter(borrowRecord -> !borrowRecord.getIsReturned())
                .collect(Collectors.toMap(
                        BorrowRecord::getId,
                        borrowRecord -> borrowRecord,
                        (existing, replacement) -> existing
                ));
    }

    @Override
    public Map<Integer, BorrowRecord> findOverdueBorrows() {
        return DataStorage.getBorrowRecords().values()
                .stream().filter(borrowRecord -> borrowRecord.getIsReturned())
                .collect(Collectors.toMap(
                        BorrowRecord::getId,
                        borrowRecord -> borrowRecord,
                        (existing, replacement) -> existing
                ));
    }

    @Override
    public List<BorrowRecord> findBorrowRecordsByMemberId(int id) {
        return DataStorage.getBorrowRecords().values()
                .stream().filter(record -> record.getMember().getId() == id)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowRecord> findActiveBorrowRecordsByMemberId(int id) {
        return DataStorage.getBorrowRecords().values()
                .stream().filter(record -> !record.getIsReturned()
                        && record.getMember().getId() == id)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BorrowRecord> findActiveBookBorrow(int bookId) {
        return DataStorage.getBorrowRecords().values().stream()
                .filter(record -> !record.getIsReturned()
                        && record.getBook().getId() == bookId)
                .findFirst();
    }

    @Override
    public List<BorrowRecord> findByBookId(int bookId) {
        return DataStorage.getBorrowRecords().values().stream()
                .filter(record -> record.getBook().getId() == bookId)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowRecord> findBorrowsByDateRange(LocalDate startDate, LocalDate endDate) {
        return DataStorage.getBorrowRecords().values().stream()
                .filter(record -> record.getBorrowDate().isAfter(startDate)
                        && record.getBorrowDate().isBefore(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public int activeBorrowsCount() {
        return (int) DataStorage.getBorrowRecords().values().stream()
                .filter(record -> !record.getIsReturned())
                .count();
    }

    @Override
    public int overdueBorrowsCount() {
        return (int) DataStorage.getBorrowRecords().values()
                .stream()
                .filter(record-> record.getIsReturned())
                .count();
    }
}
