//package repository;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.databind.type.MapType;
//import com.fasterxml.jackson.databind.type.TypeFactory;
//import common.Tuple;
//import common.User;
//import vocabulary.Selection;
//
//import java.io.File;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Hashtable;
//
//public class SelectionRepository {
//    private final String path;
//
//    public SelectionRepository(String path){
//        this.path = path;
//    }
//
//    public Hashtable<String, Selection> getAllSelection() throws IOException {
//        File file = new File(path);
//        ObjectMapper mapper = new ObjectMapper();
//        TypeFactory typeFactory = mapper.getTypeFactory();
//        MapType mapType = typeFactory.constructMapType(Hashtable.class, String.class, Selection.class);
//        return mapper.readValue(file, mapType);
//    }
//
//    public void setAllSelectionsChange(Hashtable<String, Selection> table) throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.enable(SerializationFeature.INDENT_OUTPUT);
//        File file = new File(path);
//        if (file.exists())
//            file.delete();
//        file.createNewFile();
//        mapper.writeValue(file, table);
//    }
//}
