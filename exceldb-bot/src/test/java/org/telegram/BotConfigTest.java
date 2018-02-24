package org.telegram;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.text.IsEmptyString.isEmptyString;

class BotConfigTest {

    BotConfig botConfig;

    @BeforeEach
    private void init() {
        botConfig = new BotConfig("/testconfig.properties");
    }

    @Test
    public void withConfig_excel_expectEntrySet() throws IOException {
        // Arrange
        // Act
        // Assert
        assertThat(botConfig.getExcel(), not(isEmptyString()));
    }

    @Test
    public void withConfig_excel_expectTokenSet() throws IOException {
        // Arrange
        // Act
        // Assert
        assertThat(botConfig.getExcel(), not(isEmptyString()));
    }

    @Test
    public void withConfig_excel_expectUserSet() throws IOException {
        // Arrange
        // Act
        // Assert
        assertThat(botConfig.getExcel(), not(isEmptyString()));
    }

}