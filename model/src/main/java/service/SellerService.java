package service;

import model.Match;
import model.Seller;
import repository.IRepository;

import java.util.stream.StreamSupport;

public class SellerService implements IService<Integer, Seller> {
    private IRepository repository;

    public SellerService(IRepository repository) {
        this.repository = repository;
    }

    @Override
    public Seller findOne(Integer id) {
        return (Seller) repository.findOne(id);
    }

    @Override
    public Iterable<Seller> findAll() {
        return repository.findAll();
    }

    @Override
    public void save(Seller entity) {
        repository.save(entity);
    }

    @Override
    public void delete(Integer id) {
        repository.delete(id);
    }

    @Override
    public void update(Seller entity) {
        repository.update(entity);
    }

    public boolean checkUserAndPassword(String user, String password) {
        return StreamSupport.stream(findAll().spliterator(), false)
                .anyMatch(seller -> (seller.getName().equals(user) && seller.getPassword().equals(password)));
    }
}

