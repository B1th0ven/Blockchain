package com.scor.persistance.repositories;

import org.springframework.data.repository.CrudRepository;

import com.scor.persistance.entities.RefUserEntity;


public interface UserRepository extends CrudRepository<RefUserEntity, Integer>
{
    RefUserEntity findFirstByRuLogin(String login);

    RefUserEntity findFirstByRuFirstNameContaining(String login);
}
