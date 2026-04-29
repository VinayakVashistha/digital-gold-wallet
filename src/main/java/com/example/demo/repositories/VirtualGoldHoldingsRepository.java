package com.example.demo.repositories;

import com.example.demo.model.VirtualGoldHoldings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "virtualGoldHoldings", path = "virtual-gold-holdings")
public interface VirtualGoldHoldingsRepository extends CrudRepository<VirtualGoldHoldings, Integer> {

    @RestResource(path = "by-branch", rel = "by-branch")
    List<VirtualGoldHoldings> findByBranch_BranchId(Integer branchId);

    @Override
    @RestResource(exported = false)
    <S extends VirtualGoldHoldings> S save(S entity);

    @Override
    @RestResource(exported = false)
    <S extends VirtualGoldHoldings> Iterable<S> saveAll(Iterable<S> entities);

    @Override
    @RestResource(exported = false)
    void deleteById(Integer id);

    @Override
    @RestResource(exported = false)
    void delete(VirtualGoldHoldings entity);

    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends VirtualGoldHoldings> entities);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}