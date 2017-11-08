package com.so;

import com.so.support.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class QuoteCentralTest {

    private QuotesCentral quotesCentral;

    @Mock
    private MessageServer messageServer;

    @BeforeEach
    void setup() {
        quotesCentral = new QuotesCentral(messageServer, new QuoteProvider());
    }

    @Test
    public void testQuotesShouldBePublished1() {
        ArgumentCaptor<List<Quote>> quotesCaptor = ArgumentCaptor.forClass(List.class);
        String author = "Mark Twain";
        //stub method calls
        doNothing().when(messageServer).connect();
        doNothing().when(messageServer).disconnect();
        when(messageServer.publish(any(List.class))).thenReturn(true);

        assertThat(quotesCentral.publishQuotesByAuthor(author)).isTrue();

        //verify interactions
        verify(messageServer).connect();
        verify(messageServer).publish(quotesCaptor.capture());
        assertThat(quotesCaptor.getValue()).as("There should be 2 quotes").hasSize(2);
        assertThat(quotesCaptor.getValue()).as("The author should be " + author).extracting("author").allMatch(s -> s.equals(author));
        verify(messageServer).disconnect();
    }

    @Test
    public void testQuotesShouldBePublished2() {
        ArgumentCaptor<List<Quote>> quotesCaptor = ArgumentCaptor.forClass(List.class);
        String author = "Mark Twain";

        when(messageServer.publish(any(List.class))).thenReturn(true);

        assertThat(quotesCentral.publishQuotesByAuthor(author)).isTrue();
        verify(messageServer).publish(quotesCaptor.capture());
        assertThat(quotesCaptor.getValue()).as("There should be 2 quotes").hasSize(2);
        assertThat(quotesCaptor.getValue()).as("The author should be " + author).extracting("author").allMatch(s -> s.equals(author));

        //verify interactions
        verify(messageServer).connect();
        verify(messageServer).disconnect();
    }

    @Test
    public void testQuotesShouldBePublished3() {
        ArgumentCaptor<List<Quote>> quotesCaptor = ArgumentCaptor.forClass(List.class);
        String author = "Mark Twain";

        List<Quote> markTwainQuotes = quotesCentral.findByAuthor(author);
        //markTwainQuotes.add(new Quote("bla bla","Sergiu",1));

        when(messageServer.publish(markTwainQuotes)).thenReturn(true);

        assertThat(quotesCentral.publishQuotesByAuthor(author)).isTrue();

        //verify interactions
        verify(messageServer).connect();
        verify(messageServer).disconnect();
    }
}
