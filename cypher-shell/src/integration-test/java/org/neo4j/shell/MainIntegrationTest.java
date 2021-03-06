package org.neo4j.shell;

import org.junit.Test;
import org.neo4j.shell.cli.CliArgs;
import org.neo4j.shell.log.AnsiLogger;
import org.neo4j.shell.log.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MainIntegrationTest {

    @Test
    public void connectInteractivelyPromptsOnWrongAuthentication() throws Exception {
        // given
        // what the user inputs when prompted
        String inputString = "neo4j\nneo\n";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        Main main = new Main(inputStream, ps);

        CliArgs cliArgs = new CliArgs();
        cliArgs.setUsername("", "");
        cliArgs.setPassword( "", "" );

        Logger logger = new AnsiLogger(cliArgs.getDebugMode());
        logger.setFormat(cliArgs.getFormat());

        ConnectionConfig connectionConfig = new ConnectionConfig(
                cliArgs.getScheme(),
                cliArgs.getHost(),
                cliArgs.getPort(),
                cliArgs.getUsername(),
                cliArgs.getPassword(),
                cliArgs.getEncryption());

        CypherShell shell = new CypherShell(logger);

        // when
        assertEquals("", connectionConfig.username());
        assertEquals("", connectionConfig.password());

        main.connectMaybeInteractively(shell, connectionConfig, true);

        // then
        // should be connected
        assertTrue(shell.isConnected());
        // should have prompted and set the username and password
        assertEquals("neo4j", connectionConfig.username());
        assertEquals("neo", connectionConfig.password());

        String out = baos.toString();
        assertEquals( "username: neo4j\r\n" +
                "password: ***\r\n", out );
    }
}
