package com.mitti.entrepreneur.service;

import com.mitti.entrepreneur.exception.InvalidInputException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SpringBootTest
public class MainMenuServiceImplTest {

    @Mock
    private MarketSituationService mockMarketSituationService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = InvalidInputException.class)
    public void test_initializer_failure() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Setup
        final MainMenuServiceImpl target = new MainMenuServiceImpl();
        target.setMarketSituationService(mockMarketSituationService);
        final Class mainMenuServiceImplClass = target.getClass();
        final Method method = mainMenuServiceImplClass.getDeclaredMethod("preProcess");
        method.setAccessible(true);

        Mockito.when(method.invoke(target)).thenThrow(InvalidInputException.class);

        // Execution
        target.initializer();

        // Verification

    }
}