package ru.netology.aqa;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;


import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;



public class CardDeliveryTest {
    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Test
    public void webInterfacePositiveTest() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Якутск");
        $("[data-test-id=date] input").doubleClick().sendKeys(generateDate(3));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79998887766");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=notification] .notification__content").shouldHave(text("Встреча успешно забронирована на " + generateDate(3)), Duration.ofSeconds(15)).shouldBe(visible);

    }
    @Test
    void emptyValues() {
        open("http://localhost:9999");
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=city].input_invalid").should(exactText("Поле обязательно для заполнения"));
    }


    @Test
    void enterCurrentDate() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(generateDate(0));
        $("[data-test-id=name] input").setValue("Петров Петя");
        $("[data-test-id=phone] input").setValue("+79998887767");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='date'] .input_invalid .input__sub").shouldHave(Condition.exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void invalidData() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Красноярск");
        $("[data-test-id=date] input").doubleClick().sendKeys("34.11.2023");
        $("[data-test-id=name] input").setValue("Алексеев Алексей");
        $("[data-test-id=phone] input").setValue("+79998887768");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='date'] .input_invalid .input__sub").shouldHave(Condition.exactText("Неверно введена дата"));
    }


    @Test
    void invalidName() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Благовещенск");
        $("[data-test-id=date] input").doubleClick().sendKeys(generateDate(3));
        $("[data-test-id=name] input").setValue("Ivanov Ivan");
        $("[data-test-id=phone] input").setValue("+79998887766");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(Condition.exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }


    @Test
    void invalidPhone() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Иркутск");
        $("[data-test-id=date] input").doubleClick().sendKeys(generateDate(3));
        $("[data-test-id=name] input").setValue("Владимиров Владимир");
        $("[data-test-id=phone] input").setValue("+700000000000000000");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void cityNotSupported() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Нью-Йорк");
        $("[data-test-id=date] input").doubleClick().sendKeys(generateDate(3));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79998887766");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }
}


