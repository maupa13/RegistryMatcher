package ru.sber.dto;

/**
 * Result field.
 */
public enum Result {

    problemRevenue("Отсутствуют сведения либо "
            + "размещена квартальная выручка за 2022 г."),
    filledRevenue("Cведения о выручке за 2022 г. размещены"),
    emptyRevenue("Отсутствуют сведения о выручке за 2022 г."),

    blocked223Info("Запись о блокировке: 223-ФЗ. "
            + "Рекомендуется проверить организацию вручную"),
    blocked44Info("Запись о блокировке: 44-ФЗ. "
            + "Рекомендуется проверить организацию вручную"),
    registered44And223Info("Зарегистрирован: 223-ФЗ и 44-ФЗ. "
            + "Записи о дочерних организациях отсутствуют"),
    registered223Info("Зарегистрирован: 223-ФЗ. "
            + "Записи о дочерних организациях отсутствуют"),
    registered44Info("Зарегистрирован: 44-ФЗ. "
            + "Записи о дочерних организациях отсутствуют"),
    registered44And223MotherInfo("Материнская компания зарегистрирована: "
            + "223-ФЗ и 44-ФЗ"),
    registered223MotherInfo("Зарегистрирован: 223-ФЗ. "
            + "Записи о дочерних организациях в аналогичном реестре"),
    registered44MotherInfo("Зарегистрирован: 44-ФЗ. "
            + "Записи о дочерних организациях в аналогичном реестре"),
    notRegisteredOrganizaciiInfo("Не зарегистрирован в реестре организаций"),

    notRegisteredType("Отсутствуют сведения о типе ЮЛ"),
    massRegisteredType("Множество записей о типе ЮЛ:"
            + " записи о дочерних организациях"),

    registeredCompany("Зарегистрирован"),
    massRegisteredCompany("Множество записей ЮЛ: записи о дочерних организациях"),
    notRegisteredCompany("Не зарегистрирован");

    private String info;

    Result(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return info;
    }
}
