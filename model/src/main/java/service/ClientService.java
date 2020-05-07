package service;

import model.Client;
import repository.IRepository;

import java.util.stream.StreamSupport;

public class ClientService implements IService<Integer, Client> {
    private IRepository repository;

    public ClientService(IRepository repository) {
        this.repository = repository;
    }

    @Override
    public Client findOne(Integer id) {
        return (Client) repository.findOne(id);
    }

    @Override
    public Iterable<Client> findAll() {
        return repository.findAll();
    }

    @Override
    public void save(Client entity) {
        repository.save(entity);
    }

    @Override
    public void delete(Integer id) {
        repository.delete(id);
    }

    @Override
    public void update(Client entity) {
        repository.update(entity);
    }


}
