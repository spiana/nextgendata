package com.step4.jdbcdemo.mvc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.step4.jdbcdemo.ItemResource;
import com.step4.jdbcdemo.MetaRepository;
import com.step4.jdbcdemo.Registry;
import com.step4.jdbcdemo.model.Customer;
import com.step4.jdbcdemo.model.Item;

@RestController
@RequestMapping(path = "/modelController")
public class ModelControllerImpl {

	@Resource(name="customerRepository")
	MetaRepository<Customer> customerRepository;
	
	
	@RequestMapping( path = "/{model}" , method = RequestMethod.POST , produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemResource create( @PathVariable(name="model") String typeCode , @RequestBody  Item item){
		
		MetaRepository<Item> repo = getRepository(typeCode);
		Assert.notNull(repo);
		item.typeCode = typeCode;

		
		return new ItemResource(repo.save(item));
	}
	
	
	@RequestMapping( path = "/{model}/{id}" , method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemResource findById( @PathVariable(name="model") String typeCode , @PathVariable(name="id") Long id){
		
		MetaRepository<Item> repo = getRepository(typeCode);

		Assert.notNull(repo);
		
		return new ItemResource(repo.findById(id).orElse(null));
	}
	
	
	@RequestMapping( path = "/{model}/all" , method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public CollectionModel<ItemResource>  findAll( @PathVariable(name="model") String typeCode){
		
		MetaRepository<Item> repo = getRepository(typeCode);

		Assert.notNull(repo);
		
		return new CollectionModel<ItemResource>(repo.findAll().stream().map(ItemResource::new).collect(Collectors.toList()));
	}
	
	@RequestMapping( path = "/{model}/{columnName}/{id}" , method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public CollectionModel<ItemResource>  findAllByColumnId( @PathVariable(name="model") String typeCode, 
			@PathVariable(name="columnName") String columnName ,@PathVariable(name="id") Long id ){
		
		MetaRepository<Item> repo = getRepository(typeCode);

		Assert.notNull(repo);
		
		return new CollectionModel<ItemResource>(repo.findAllByColumnId(columnName, id).stream().map(ItemResource::new).collect(Collectors.toList()));
	}
	
	@RequestMapping( path = "/{model}/page" , method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public PagedModel<ItemResource> findPage( @PathVariable(name="model") String typeCode ,@PageableDefault org.springframework.data.domain.Pageable p, PagedResourcesAssembler pagedAssembler) throws NoSuchMethodException, SecurityException{
	
		MetaRepository<Item> repo = getRepository(typeCode);
		Assert.notNull(repo);
		
		Page<Item> pages = repo.findAll(p);
		  return pagedAssembler.toModel(pages);
	}
	
	
	
	
	private MetaRepository<Item> getRepository(String typeCode){
		MetaRepository<Item> repo = Registry.getRegistry().getBean(typeCode + "Repository" , MetaRepository.class);
		Assert.notNull(repo);
		
		return repo;
		
	}
}
