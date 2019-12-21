console.log(item.getProperties());
//item.getProperties().put("customField" , "prova di inserimento utente con preCallBack") ;
item.putProperty("customField" , "questa è una prova di inserimento utente di nome :( " + item.getProperty("firstName")+ ") !!");
console.log(item.getProperties());
