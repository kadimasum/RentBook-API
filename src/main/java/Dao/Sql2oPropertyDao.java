package Dao;

import modules.Property;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oPropertyDao implements IPropertyDao{

    private final Sql2o sql2o;

    public Sql2oPropertyDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Property property) {
        String sql = "INSERT INTO properties (name,location, number_of_units, rent_per_unit, has_electricity, has_water, has_internet, caretaker_name, caretaker_phone_number, landlord_id ) VALUES (:name, :location, :number_of_units, :rent_per_unit, :has_electricity, :has_water, :has_internet, :caretaker_name, :caretaker_phone_number, :landlord_id)";
        try(Connection con = sql2o.open()){
            int id = (int) con.createQuery(sql, true)
                    .bind(property)
                    .executeUpdate()
                    .getKey();
            property.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public List<Property> getAll() {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM properties")
                    .executeAndFetch(Property.class);
        }
    }

    @Override
    public Property findById(int id) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM properties WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Property.class);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from properties WHERE id = :id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public void clearAll() {
        String sql = "DELETE from properties";
        String resetSql = "ALTER SEQUENCE property_id_seq RESTART WITH 1;";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql).executeUpdate();
            con.createQuery(resetSql).executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }
}