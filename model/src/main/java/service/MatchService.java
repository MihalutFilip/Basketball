package service;

import model.Client;
import model.Match;
import repository.IRepository;

public class MatchService implements IService<Integer, Match> {
    private IRepository repository;

    public MatchService(IRepository repository) {
        this.repository = repository;
    }

    @Override
    public Match findOne(Integer id) {
        return (Match) repository.findOne(id);
    }

    @Override
    public Iterable<Match> findAll() {
        return repository.findAll();
    }

    @Override
    public void save(Match entity) {
        repository.save(entity);
    }

    @Override
    public void delete(Integer id) {
        repository.delete(id);
    }

    @Override
    public void update(Match entity) {
        repository.update(entity);
    }
}
