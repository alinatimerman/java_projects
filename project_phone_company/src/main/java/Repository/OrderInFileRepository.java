package Repository;

import Model.Order;
import Model.Phone;

import java.io.*;

public class OrderInFileRepository extends OrderInMemoRepository{
    private String file;
    private static int idGenerator = 0;

    public OrderInFileRepository(String file, PhoneRepository phoneRepository){
        this.file = file;
        retrieveOrder();
    }

    private void retrieveOrder() {
        try(BufferedReader buffer = new BufferedReader(new FileReader(file))){
            String line = buffer.readLine();
            try{
                idGenerator=Integer.parseInt(line);
            }catch (NumberFormatException ex){
                System.err.println("Invalid Value for idGenerator");
            }
            while((line = buffer.readLine()) != null){
                String[] attributes = line.split("; ");
                if (attributes.length != 4){
                    System.out.println(line);
                    System.out.println(attributes.length);
                    System.err.println("Invalid line ..." + line);
                    continue;
                }
                try{
                    int id = Integer.parseInt(attributes[0]);
                    int phone_id = Integer.parseInt(attributes[1]);
                    Order o = new Order(id, phone_id,attributes[2],attributes[3]);
                    super.add(o);
                }catch(NumberFormatException ex){
                    System.err.println("Error converting number");
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }catch(IOException exception){
            throw new RepositoryException("Reading error " + exception);
        }

    }

    private void storeOrder() {
        try(BufferedWriter buffer = new BufferedWriter(new FileWriter(file))){
            buffer.write("" + idGenerator);
            buffer.newLine();
            for(Order o : findAll()){
                buffer.write(o.toString());
                buffer.newLine();
            }
        } catch (IOException exception) {
            throw new RepositoryException("Writing error " + exception);
        }
    }

    @Override
    public Order add(Order o){
        o.setId(getNextId());
        super.add(o);
        storeOrder();
        return o;
    }

    @Override
    public void delete(Order o){
        super.delete(o);
        storeOrder();
    }

    @Override
    public void update(Integer id, Order o){
        super.update(id, o);
        storeOrder();
    }

    public int getNextId(){
        return idGenerator++;
    }

    public int getIdGenerator(){
        return idGenerator;
    }
}
