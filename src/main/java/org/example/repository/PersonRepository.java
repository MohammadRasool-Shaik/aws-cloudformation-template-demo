/*
package org.example.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Person;

@Slf4j
@RequiredArgsConstructor
public class PersonRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public Person savePerson(Person person) {
        dynamoDBMapper.save(person);
        return person;
    }

    public Person findById(String personId) {
        return dynamoDBMapper.load(Person.class, personId);
    }

    public void deletePerson(Person person) {
        dynamoDBMapper.delete(person);
    }


    */
/*public Person updatePerson(String personId, Person person) {

    }*//*

}
*/
