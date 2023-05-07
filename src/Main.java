import index.Index;
import key.Key;
import nodes.Leaf;
import tables.CustomerAccount;
import tables.OrdinaryTable;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean terminate = false;

        String path = "./src/testExamples/CustomerAccount20.txt";
        CustomerAccount typeObject = new CustomerAccount();
        Index<CustomerAccount> index = null;

        //String path = "./src/testExamples/table1.txt";
        //OrdinaryTable typeObject = new OrdinaryTable();
        //Index<OrdinaryTable> index = null;

        File file = new File(path);

        try (Scanner sc = new Scanner(System.in)) {
            while (!terminate) {
                System.out.println("User menu: ");
                System.out.println("""
                        0 - Exit
                        1 - Create new index or delete one
                        2 - Print index
                        3 - Add new record in table
                        4 - Delete record from table
                        5 - Search for one key
                        6 - Search for k keys
                        7 - Search for k successive keys
                        """);

                int option = sc.nextInt();
                switch (option) {
                    case 0: {
                        terminate = true;
                        break;
                    }

                    case 1: {
                        System.out.println("1 - Make new index\n" +
                                "2 - Delete index");

                        int additionalOption = sc.nextInt();
                        if (additionalOption == 1) {
                            System.out.println("Enter m");
                            int m = sc.nextInt();

                            index = Main.createNewIndex(m, typeObject, file);
                        }

                        else {
                            index = Main.deleteIndex(typeObject);
                            System.out.println(index);
                        }

                        break;
                    }

                    case 2: {
                        if(index != null)
                            index.print();

                        break;
                    }

                    case 3: {
                        assert index != null;
                        Main.addRecord(index, typeObject, file, sc);
                        break;
                    }

                    case 4: {
                        assert index != null;
                        Main.removeRecord(index, sc);
                        break;
                    }

                    case 5: {
                        assert index != null;
                        Main.searchForOneKey(index, sc);
                        break;
                    }

                    case 6: {
                        assert index != null;
                        Main.searchForKKeys(index, sc);
                        break;
                    }

                    case 7: {
                        assert index != null;
                        Main.searchForKSuccessiveKeys(index, sc);
                        break;
                    }
                    
                    default: {
                        System.out.println("Wrong case number.");
                        break;
                    }
                }
            }
        }
    }

    private static <T> void searchForKSuccessiveKeys(Index<T> index, Scanner sc) {
        System.out.println("Enter k and start key:");
        int k = sc.nextInt();
        long startValue = sc.nextLong();

        Leaf<T> searchedLeaf = index.searchLeaf(startValue);
        List<Leaf<T>> leavesList = index.getLeavesList();
        int searchedLeafIndex = leavesList.indexOf(searchedLeaf);
        List<Leaf<T>> searchableLeavesList = leavesList.subList(searchedLeafIndex, leavesList.size());

        int values = 0;
        boolean exitOuterFor = false;
        boolean firstKeyMet = false;
        for(Leaf<T> leaf : searchableLeavesList) {
            for(Key<T> key: leaf.getKeys()) {
                if(!firstKeyMet) {
                    if(key.getValue() == startValue) {
                        firstKeyMet = true;
                    }

                    else {
                        continue;
                    }
                }

                if(values == k) {
                    exitOuterFor = true;
                    break;
                }
                values++;

                System.out.println(key.getContent());
            }

            if(exitOuterFor) {
                break;
            }
        }

        if(!firstKeyMet) {
            System.out.println("Start key not found.");
        }
    }

    private static <T> void searchForKKeys(Index<T> index, Scanner sc) {
        System.out.println("Enter k:");
        int k = sc.nextInt();

        System.out.println("Enter k search keys:");
        for(int i = 0 ; i < k ; i++) {
            long value = sc.nextLong();
            boolean keyFound = false;
            Leaf<T> searchedLeaf = index.searchLeaf(value);
            for(Key<T> key: searchedLeaf.getKeys()) {
                if(key.getValue() == value) {
                    System.out.println(key.getContent());
                    keyFound = true;
                }
            }

            if(!keyFound) {
                System.out.println("Key not found.");
            }
        }
    }

    private static <T> void searchForOneKey(Index<T> index, Scanner sc) {
        System.out.println("Enter search key: ");
        long value = sc.nextLong();
        boolean keyFound = false;
        Leaf<T> searchedLeaf = index.searchLeaf(value);
        for(Key<T> key: searchedLeaf.getKeys()) {
            if(key.getValue() == value) {
                System.out.println(key.getContent());
                keyFound = true;
            }
        }

        if(!keyFound) {
            System.out.println("Key not found.");
        }
    }

    private static <T> void removeRecord(Index<T> index, Scanner sc) {
        System.out.println("Enter key to delete:");
        long value = sc.nextLong();
        index.deleteKey(value);
        //delete actual key in table
    }

    private static <T> void addRecord(Index<T> index, T typeObject, File file, Scanner sc) {
        try(BufferedWriter out = new BufferedWriter(new FileWriter(file, true))) {
            System.out.println("Enter row:");

            sc.nextLine();
            String line = sc.nextLine();
            String[] row = line.split("\\|");

            long key = Long.parseLong(row[0]);

            T content = (T) Key.createContents(typeObject.getClass(), line);
            Key<T> newKey = new Key<>(key, content);
            index.insertInLeaf(newKey);

            //insertActualKeyInTable
        } catch (IOException e) {
            System.out.println("Write file not found");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            System.out.println("Content error");
        }
    }

    private static <T> Index<T> deleteIndex(T typeObject) {
        return null;
    }

    private static <T> Index<T> createNewIndex(int m, T typeObject, File file) {
        try(BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;
            List<Key<T>> keys = new LinkedList<>();

            while((line = in.readLine()) != null) {
                String[] row = line.split("\\|");
                long key = Long.parseLong(row[0]);

                T content = (T) Key.createContents(typeObject.getClass(), line);
                keys.add(new Key<>(key, content));
            }

            Index<T> index = new Index<>(m, keys);
            index.insertTableValues();

            return index;
        } catch (IOException e) {
            System.out.println("Read file not found.");
            return null;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Content error");
            return null;
        }
    }
}
