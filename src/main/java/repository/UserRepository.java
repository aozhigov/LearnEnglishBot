//package repository;
//
//import com.fasterxml.jackson.databind.type.MapType;
//import com.fasterxml.jackson.databind.type.TypeFactory;
//import common.User;
//
//import com.fasterxml.jackson.core.JsonGenerationException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Hashtable;
//
//
//public class UserRepository {
//    private final String path;
//
//    public UserRepository(String path){
//        this.path = path;
//    }
//
//    public Hashtable<Long, User> getAllUser() throws IOException {
//        File file = new File(path);
//        ObjectMapper mapper = new ObjectMapper();
//        TypeFactory typeFactory = mapper.getTypeFactory();
//        MapType mapType = typeFactory.constructMapType(Hashtable.class, Long.class, User.class);
//        return mapper.readValue(file, mapType);
//    }
//
//    public void setAllUserChange(Hashtable<Long, User> users) throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.enable(SerializationFeature.INDENT_OUTPUT);
//        File file = new File(path);
//        if (file.exists())
//            file.delete();
//        file.createNewFile();
//        mapper.writeValue(file, users);
//    }
//}
