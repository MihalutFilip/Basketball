package service;

import javafx.util.Pair;
import model.Seller;
import model.Ticket;
import repository.IRepository;

public class TicketService implements IService<Pair, Ticket> {
    private IRepository repository;

    public TicketService(IRepository repository) {
        this.repository = repository;
    }

    @Override
    public Ticket findOne(Pair id) {
        return (Ticket) repository.findOne(id);
    }

    @Override
    public Iterable<Ticket> findAll() {
        return repository.findAll();
    }

    @Override
    public void save(Ticket entity) {
        repository.save(entity);
    }

    @Override
    public void delete(Pair id) {
        repository.delete(id);
    }

    @Override
    public void update(Ticket entity) {
        repository.update(entity);
    }
}