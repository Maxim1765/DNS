import java.io.*;
import java.util.Scanner;

public class MainSHOP {
    //region utils methods
    static String inputString(String message) {
        boolean isValidInput;
        String output = "";
        do {
            try {
                isValidInput = true;
                Scanner scanner = new Scanner(System.in);

                System.out.print(message);
                output = scanner.nextLine();
            } catch (Exception e) {
                isValidInput = false;
                System.out.println("Ошибка ввода. Пожалуйста повторите ввод");
            }

        } while (isValidInput == false);

        return output;
    }

    static int inputInt(String message, int min, int max) {
        boolean isValidInput;
        int output = 0;
        do {
            try {
                isValidInput = true;
                Scanner scanner = new Scanner(System.in);

                System.out.print(message);
                output = scanner.nextInt();

                if (output < min || output > max) {
                    System.out.println("Ошибка ввода. Вы вышли за границы диапазона от " + min + " до " + max);
                    throw new Exception();
                }

            } catch (Exception e) {
                isValidInput = false;
                System.out.println("Ошибка ввода. Пожалуйста повторите ввод");
            }

        } while (isValidInput == false);

        return output;
    }
//endregion

    //region global variables
    enum SortDirection {
        ASC,
        DESC
    }

    static int lastPhoneId = 0;

    static class Phone {
        public int id;
        public String model;
        public int price;
        public int amount;

        public Phone(int id, String model, int price, int amount) {
            this.id = id;
            this.model = model;
            this.price = price;
            this.amount = amount;
        }
    }
//endregion

    //region support phones methods
    static Phone getPhoneById(Phone[] phones, int id) {
        for (int i = 0; i < phones.length; i++) {
            if (phones[i].id == id) {
                return phones[i];
            }
        }
        return null;
    }

    static int getIndexPhoneById(Phone[] phones, int id) {
        for (int i = 0; i < phones.length; i++) {
            if (phones[i].id == id) {
                return i;
            }
        }
        return -1;
    }
//endregion

    //region main phones methods
    static Phone createPhone() {
        String model = inputString("Введите модель: ");

        int price = inputInt("Введите цену: ", 1, 1000000);

        int amount = inputInt("Введите количество на складе: ", 1, 10000);

        return new Phone(0, model, price, amount);
    }

    static Phone[] createEmptyPhonesArray() {
        return new Phone[0];
    }

    static Phone[] addPhoneToEndOfArray(Phone[] phones, Phone insertedPhone) {


        Phone[] tempPhones = new Phone[phones.length + 1];

        for (int i = 0; i < phones.length; i++) {
            tempPhones[i] = phones[i];
        }

        tempPhones[tempPhones.length - 1] = insertedPhone;


        return tempPhones;
    }

    static void updatePhoneById(Phone[] phones, int updatedIdPhone) {
        Phone foundPhone = getPhoneById(phones, updatedIdPhone);

        if (foundPhone == null) {
            System.out.println("Ошибка. Товар с таким ID не найден.");
            return;
        }

        System.out.println("Введите данные товара для обновления");
        Phone updatedPhone = createPhone();

        foundPhone.model = updatedPhone.model;
        foundPhone.price = updatedPhone.price;
        foundPhone.amount = updatedPhone.amount;

        System.out.println("Товар обновлён успешно!");
    }

    static Phone[] deletePhoneById(Phone[] phones, int deletedIdPhone) {
        int foundPhoneIndex = getIndexPhoneById(phones, deletedIdPhone);

        if (foundPhoneIndex == -1) {
            System.out.println("Ошибка. Товар с таким ID не найден. Удаление не возможно");
            return phones;
        }

        Phone[] tempPhones = new Phone[phones.length - 1];
        int tempPhonesIndex = 0;

        for (int i = 0; i < phones.length; i++) {
            if (i != foundPhoneIndex) {
                tempPhones[tempPhonesIndex] = phones[i];
                tempPhonesIndex++;
            }
        }

        System.out.println("Товар удалён успешно!");

        return tempPhones;
    }

    static Phone[] sellPhone(Phone[] phones, int soldIdPhone) {
        Phone foundPhone = getPhoneById(phones, soldIdPhone);

        if (foundPhone == null) {
            System.out.println("Ошибка. Товар с таким ID не найден. Продажа не возможна");
            return phones;
        }

        int soldAmount = inputInt("Введите кол-во товара для покупки: ", 1, foundPhone.amount);

        foundPhone.amount -= soldAmount;

        if (foundPhone.amount == 0) {
            phones = deletePhoneById(phones, foundPhone.id);
        }

        System.out.println("Покупка осуществлена успешно!");

        return phones;
    }

    static Phone[] findPhonesByPrice(Phone[] phones, int minPrice, int maxPrice) {
        Phone[] foundPhones = createEmptyPhonesArray();

        for (int i = 0; i < phones.length; i++) {
            if (phones[i].price >= minPrice && phones[i].price <= maxPrice) {
                foundPhones = addPhoneToEndOfArray(foundPhones, phones[i]);
            }
        }

        return foundPhones;
    }

    static void sortPhonesByAmount(Phone[] phones, SortDirection sortDirection) {
        for (int i = 0; i < phones.length - 1; i++) {
            int index = i;
            for (int j = i + 1; j < phones.length; j++) {

                if (sortDirection == SortDirection.ASC) {
                    if (phones[j].amount < phones[index].amount) {
                        index = j;
                    }
                } else if (sortDirection == SortDirection.DESC) {
                    if (phones[j].amount > phones[index].amount) {
                        index = j;
                    }
                }
            }
            Phone temp = phones[index];
            phones[index] = phones[i];
            phones[i] = temp;
        }
    }

    static void savePhonesToTxtFile(String filename, Phone[] phones) throws IOException {
        FileWriter fileWriter = new FileWriter(filename);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        bufferedWriter.write(Integer.toString(phones.length));
        bufferedWriter.newLine();

        for (int i = 0; i < phones.length; i++) {
            bufferedWriter.write(Integer.toString(phones[i].id));
            bufferedWriter.newLine();

            bufferedWriter.write(phones[i].model);
            bufferedWriter.newLine();

            bufferedWriter.write(Integer.toString(phones[i].price));
            bufferedWriter.newLine();

            bufferedWriter.write(Integer.toString(phones[i].amount));
            bufferedWriter.newLine();
        }
        bufferedWriter.write(Integer.toString(lastPhoneId));
        bufferedWriter.newLine();


        bufferedWriter.close();
        fileWriter.close();

        System.out.println("Сохранение совершено успешно!");
    }

    static Phone[] loadPhonesFromTxtFile(String filename) throws IOException {
        FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        int phonesLength = Integer.parseInt(bufferedReader.readLine());
        Phone[] phones = new Phone[phonesLength];

        for (int i = 0; i < phones.length; i++) {
            int id = Integer.parseInt(bufferedReader.readLine());
            String model = bufferedReader.readLine();
            int price = Integer.parseInt(bufferedReader.readLine());
            int amount = Integer.parseInt(bufferedReader.readLine());

            phones[i] = new Phone(id, model, price, amount);
        }

        lastPhoneId = Integer.parseInt(bufferedReader.readLine());

        bufferedReader.close();
        fileReader.close();

        System.out.println("Загрузка совершена успешно!");

        return phones;
    }

    static void printPhonesToTxtFile(String filename, Phone[] phones) throws IOException {
        FileWriter fileWriter = new FileWriter(filename);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        bufferedWriter.write(String.format("%-3s%-12s%-8s%-18s", "ИД", "Модель", "Цена", "Остаток на складе"));
        bufferedWriter.newLine();

        if (phones.length == 0) {
            bufferedWriter.write("Список товаров пуст");
            bufferedWriter.newLine();
        } else {
            for (int i = 0; i < phones.length; i++) {
                bufferedWriter.write(String.format("%-3d%-12s%-8d%-18d", phones[i].id, phones[i].model, phones[i].price, phones[i].amount));
                bufferedWriter.newLine();
            }
            bufferedWriter.write("Список товаров насчитывает " + phones.length + " элем.");
        }

        bufferedWriter.close();
        fileWriter.close();

        System.out.println("Печать в файл совершена успешно!");
    }
//endregion

    //region ui methods
    static void printlnTableHeader() {
        System.out.println(String.format("%-3s%-12s%-8s%-18s", "ИД", "Модель", "Цена", "Остаток на складе"));
    }

    static void printlnPhone(Phone phone) {
        System.out.println(String.format("%-3d%-12s%-8d%-18d", phone.id, phone.model, phone.price, phone.amount));
    }

    static void printlnPhones(Phone[] phones) {
        if (phones.length == 0) {
            System.out.println("Список товаров пуст");
            return;
        }

        for (int i = 0; i < phones.length; i++) {
            printlnPhone(phones[i]);
        }
        System.out.println("Список телефонов насчитывает " + phones.length + " элем.");
    }

    static void printlnSeparator() {
        System.out.println("-".repeat(15));
    }

    static void printlnMenu() {
        System.out.println("Меню");
        System.out.println("1. Добавить новый товар");
        System.out.println("2. Сохранить товары в текстовый файл");
        System.out.println("3. Загрузить товары из текстового файла");
        System.out.println("4. Осуществить продажу товара");
        System.out.println("5. Удалить товар из списка");
        System.out.println("6. Распечатать список товаров в файл");
        System.out.println("7. Обновить данные о товаре");
        System.out.println("8. Найти товары в заданном ценовом диапазоне");
        System.out.println("9. Отсортировать товары по остатку на складе");
        System.out.println("0. Выйти из программы");
    }
//endregion

    public static void main(String[] args) throws IOException {
        Phone[] phones = createEmptyPhonesArray();

        while (true) {
            printlnTableHeader();
            printlnPhones(phones);
            printlnSeparator();
            printlnMenu();
            int menuPoint = inputInt("Введите номер нужного пункта меню: ", 0, 9);

            switch (menuPoint) {
                case 1: {
                    System.out.println("Что вы хотите добавить?");
                    System.out.println("1.телефон");
                    System.out.println("2. телевизор");
                    System.out.println("3.бытовая техника");
                    System.out.println("4.техника для промышленного производства");
                    System.out.println("5.комплектующие для пк");
                    System.out.println("6.серверные комплектующие");
                    int inputGas = inputInt("Введите номер нужного пункта меню", 1 , 6);

                    switch (inputGas){
                        case 1:{
                            Phone insertedPhone = createPhone();

                            lastPhoneId++;
                            insertedPhone.id = lastPhoneId;

                            phones = addPhoneToEndOfArray(phones, insertedPhone);

                            System.out.println("Товар добавлен успешно!");
                        }
                        break;


                        case 2:{
                            Phone insertedPhone = createPhone();

                            lastPhoneId++;
                            insertedPhone.id = lastPhoneId;

                            phones = addPhoneToEndOfArray(phones, insertedPhone);

                            System.out.println("Товар добавлен успешно!");
                        }
                        break;

                        case 3:{
                            Phone insertedPhone = createPhone();

                            lastPhoneId++;
                            insertedPhone.id = lastPhoneId;

                            phones = addPhoneToEndOfArray(phones, insertedPhone);

                            System.out.println("Товар добавлен успешно!");
                        }
                        break;

                        case 4:{
                            Phone insertedPhone = createPhone();

                            lastPhoneId++;
                            insertedPhone.id = lastPhoneId;

                            phones = addPhoneToEndOfArray(phones, insertedPhone);

                            System.out.println("Товар добавлен успешно!");
                        }
                        break;

                        case 5:{
                            Phone insertedPhone = createPhone();

                            lastPhoneId++;
                            insertedPhone.id = lastPhoneId;

                            phones = addPhoneToEndOfArray(phones, insertedPhone);

                            System.out.println("Товар добавлен успешно!");
                        }
                        break;

                        case 6:{
                            Phone insertedPhone = createPhone();

                            lastPhoneId++;
                            insertedPhone.id = lastPhoneId;

                            phones = addPhoneToEndOfArray(phones, insertedPhone);

                            System.out.println("Товар добавлен успешно!");
                        }
                        break;
                    }

                }
                break;

                case 2: {
                    String filename = inputString("Введите имя файла: ");

                    savePhonesToTxtFile(filename, phones);
                }
                break;

                case 3: {
                    String filename = inputString("Введите имя файла: ");

                    phones = loadPhonesFromTxtFile(filename);
                }
                break;

                case 4: {
                    int soldIdPhone = inputInt("Введите ID телефона для покупки: ", 1, lastPhoneId);
                    phones = sellPhone(phones, soldIdPhone);
                }
                break;

                case 5: {
                    int deletedIdPhone = inputInt("Введите ID телефона для удаления : ", 1, lastPhoneId);
                    phones = deletePhoneById(phones, deletedIdPhone);
                }
                break;

                case 6: {
                    String filename = inputString("Введите имя файла: ");

                    printPhonesToTxtFile(filename, phones);
                }
                break;

                case 7: {
                    int updatedIdPhone = inputInt("Введите ID телефона для обновления : ", 1, lastPhoneId);
                    updatePhoneById(phones, updatedIdPhone);
                }
                break;

                case 8: {
                    int minPrice = inputInt("Введите минимальную цену телефона: ", 1, 1000000);
                    int maxPrice = inputInt("Введите максимальную цену телефона: ", 1, 1000000);
                    Phone[] foundPhones = findPhonesByPrice(phones, minPrice, maxPrice);

                    printlnTableHeader();
                    printlnPhones(foundPhones);
                }
                break;

                case 9: {
                    int sortDirection = inputInt("Введите направление сортировки(0 - по возрастанию, 1 - по убыванию): ", 0, 1);

                    sortPhonesByAmount(phones, SortDirection.values()[sortDirection]);
                }
                break;

                case 0: {
                    System.exit(0);
                }
                break;
            }
        }
    }
}