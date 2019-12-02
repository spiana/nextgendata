package com.step4.jdbcdemo;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.step4.jdbcdemo.model.Item;

public class ItemDeserializer extends JsonDeserializer<Item> {

	PersistanceDictionary dictionary;

	public ItemDeserializer() {
		this.dictionary = Registry.getRegistry().getBean(PersistanceDictionary.class);
	}

	@Override
	public Item deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Item item = new Item();
		JsonNode node = p.getCodec().readTree(p);

		Long pk = node.get("pk") != null ? node.get("pk").asLong() : null;
		String typeCode = node.get("typeCode").asText();

		PersistenceEntity entity = dictionary.get(typeCode);

		ObjectMapper mapper = new ObjectMapper();

		item.setPk(pk);
		item.setTypeCode(typeCode);

		if (node.get("properties") != null) {
			Iterator<Map.Entry<String, JsonNode>> iterator = node.get("properties").fields();
			while (iterator.hasNext()) {
				Entry<String, JsonNode> e = iterator.next();
				String k = e.getKey();
				PersistanceAttribute a = entity.getAttributes().stream().filter(o -> o.name.equals(k)).findFirst()
						.orElse(null);
				if (a == null)
					continue;

				if (a.relationType.equals(RelationType.NONE)) {
					try {
						if (!Item.class.isAssignableFrom(Class.forName(a.type))) {
							item.addProperty(k, mapper.convertValue(e.getValue(), Class.forName(a.type)));
						} else {
							item.addProperty(k, mapper.convertValue(e.getValue(), Item.class));
						}

					} catch (ClassNotFoundException e1) {
						// TODO: handle exception
					}

					entity.getIds();
				}

			}
		}

		return item;
	}

}
