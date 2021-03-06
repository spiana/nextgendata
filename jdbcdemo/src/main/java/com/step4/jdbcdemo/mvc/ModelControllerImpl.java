package com.step4.jdbcdemo.mvc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import com.step4.jdbcdemo.model.AbstractItem;

@RestController
@RequestMapping(path = "/modelController")
public class ModelControllerImpl<S extends AbstractItem<ID> , ID> {

	
	
	@RequestMapping( path = "/{model}" , method = RequestMethod.POST , produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemResource create( @PathVariable(name="model") String typeCode , @RequestBody  S item){
		
		MetaRepository<S , ID> repo = getRepository(typeCode);
		Assert.notNull(repo);
		item.typeCode = typeCode;
		repo.save(item);
		
		return new ItemResource(repo.findById(item.getId()).orElse(null));
	}
	
	
	@RequestMapping( path = "/{model}/{id}" , method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemResource findById( @PathVariable(name="model") String typeCode , @PathVariable(name="id") ID id){
		
		MetaRepository<S , ID> repo = getRepository(typeCode);

		Assert.notNull(repo);
		
		return new ItemResource(repo.findById(id).orElse(null));
	}
	
	
	@RequestMapping( path = "/{model}/all" , method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public CollectionModel<ItemResource>  findAll( @PathVariable(name="model") String typeCode){
		
		MetaRepository<S , ID> repo = getRepository(typeCode);

		Assert.notNull(repo);
		
		return new CollectionModel<ItemResource>(repo.findAll().stream().map(ItemResource::new).collect(Collectors.toList()));
	}
	
	@RequestMapping( path = "/{model}/{columnName}/{id}" , method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public CollectionModel<ItemResource>  findAllByColumnId( @PathVariable(name="model") String typeCode, 
			@PathVariable(name="columnName") String columnName ,@PathVariable(name="id") ID id ){
		
		MetaRepository<S , ID> repo = getRepository(typeCode);

		Assert.notNull(repo);
		
		return new CollectionModel<ItemResource>(repo.findAllByColumnId(columnName, id).stream().map(ItemResource::new).collect(Collectors.toList()));
	}
	
	@RequestMapping( path = "/{model}/page" , method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public PagedModel<ItemResource> findPage( @PathVariable(name="model") String typeCode ,@PageableDefault org.springframework.data.domain.Pageable p, PagedResourcesAssembler pagedAssembler) throws NoSuchMethodException, SecurityException{
	
		MetaRepository repo = getRepository(typeCode);
		Assert.notNull(repo);
		
		Page<AbstractItem> pages = repo.findAll(p);
		
		Page<ItemResource> _p = new PageImpl<ItemResource>(pages.getContent().stream().map(ItemResource::new).collect(Collectors.toList()),pages.getPageable(), pages.getTotalElements());
	
				
		return pagedAssembler.toModel(_p);
	}
	
	
	@RequestMapping( path = "/{model}/search" , method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public CollectionModel<ItemResource> search( @PathVariable(name="model") String typeCode , @RequestParam(name="q") String query,@PageableDefault org.springframework.data.domain.Pageable p, PagedResourcesAssembler pagedAssembler) throws NoSuchMethodException, SecurityException{
	
		MetaRepository<S , ID> repo = getRepository(typeCode);
		Assert.notNull(repo);
		
		return new CollectionModel<ItemResource>( repo.search(query).stream().map(ItemResource::new).collect(Collectors.toList()));

	}
	
	
	private <S extends AbstractItem<ID> , ID> MetaRepository<S , ID> getRepository(String typeCode){
		MetaRepository repo = Registry.getRegistry().getBean(typeCode + "Repository" , MetaRepository.class);
		Assert.notNull(repo);
		
		return repo;
		
	}
}
