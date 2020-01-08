package com.step4.jdbcdemo.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.step4.jdbcdemo.PersistanceDictionary;
import com.step4.jdbcdemo.PersistenceEntity;
import com.step4.jdbcdemo.RepositoryFactory;

@RestController
@RequestMapping("/dictionary")
public class DataDictionaryController {

	@Autowired
	PersistanceDictionary dictionary;
	
	@Autowired
	RepositoryFactory factory;

	@GetMapping
	public PersistanceDictionary getAllDictionary() {
		return dictionary;
	}
	
	@GetMapping("/{typecode}")
	public PersistenceEntity getDictionary(@PathVariable(name="typecode") String typeCode) {

		return dictionary.get(typeCode);
	}
	@PostMapping("/{typecode}")
	public PersistenceEntity postDictionary(@PathVariable(name="typecode") String typeCode , @RequestBody() PersistenceEntity entity ) {
		dictionary.put(typeCode, entity);
		
		factory.registerRepositories();
		
		
		return dictionary.get(typeCode);
	}
	
	@PutMapping("/{typecode}")
	public PersistenceEntity putDictionary(@PathVariable(name="typecode") String typeCode , @RequestBody() PersistenceEntity entity ) {
		dictionary.remove(typeCode);
		dictionary.put(typeCode, entity);
		factory.registerRepositories();
		return dictionary.get(typeCode);
	}
	
	@DeleteMapping("/{typecode}")
	public PersistenceEntity deleteDictionary(@PathVariable(name="typecode") String typeCode  ) {
		dictionary.remove(typeCode);
		factory.registerRepositories();
		return dictionary.get(typeCode);
	}
}
