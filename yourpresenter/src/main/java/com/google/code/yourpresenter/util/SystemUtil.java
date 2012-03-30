package com.google.code.yourpresenter.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.exec.environment.EnvironmentUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;

public class SystemUtil {

	private static Logger logger = LoggerFactory.getLogger(SystemUtil.class);

	public static int exec(String cmd, List<String> arguments,
			Map<String, Object> envVars, OutputStream output, File file,
			OutputStream error, boolean quiet) throws YpException {
		InputStream fis = null;
		try {
			fis = new FileInputStream(file);
			return exec(cmd, arguments, envVars, output, fis, error, quiet);
		} catch (FileNotFoundException e) {
			throw new YpException(YpError.EXEC_FAILED, e);
		} finally {
			if (null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					// supress errors
				}
				fis = null;
			}
		}
	}

	/**
	 * Executes command with specified <code>arguments</code>,
	 * <code>envVars</code> where outout goes to <code>output</code> and error
	 * output goes to <code>error</code>. Return value is the returned value of
	 * the executed command.
	 * 
	 * @param cmd
	 * @param arguments
	 * @param envVars
	 * @param output
	 * @param error
	 * @param quiet
	 *            TODO
	 * @return
	 * @throws IOException
	 * @throws SASAppException
	 */
	public static int exec(String cmd, List<String> arguments,
			Map<String, Object> envVars, OutputStream output,
			InputStream input, OutputStream error, boolean quiet)
			throws YpException {

		if (StringUtils.isEmpty(cmd)) {
			throw new YpException(YpError.EXEC_FAILED,
					"Empty command recieved!");
		}

		DefaultExecutor executor = new DefaultExecutor();
		executor.setStreamHandler(new PumpStreamHandler(output, error, input));
		CommandLine cmdLine = CommandLine.parse(cmd);

		if (null != arguments) {
			cmdLine.addArguments(arguments.toArray(new String[arguments.size()]));
		}

		@SuppressWarnings("rawtypes")
		Map env = null;
		try {
			env = EnvironmentUtils.getProcEnvironment();
		} catch (IOException e) {
			throw new YpException(YpError.EXEC_FAILED, e);
		}

		if (null != envVars) {
			logger.debug("Execution environment: ", envVars.toString());

			for (Entry<String, Object> e : envVars.entrySet()) {
				EnvironmentUtils.addVariableToEnvironment(
						env,
						new StringBuilder(e.getKey()).append("=")
								.append(e.getValue()).toString());
			}
		}

		logger.debug("Executing command: ", cmdLine.toString());

		int exitValue = 0;
		try {
			exitValue = executor.execute(cmdLine, env);
		} catch (ExecuteException e) {
			logger.error("Executed command errors: \n", error);
			throw new YpException(YpError.EXEC_FAILED, e);
		} catch (IOException e) {
			logger.error("Executed command errors: \n", error);
			throw new YpException(YpError.EXEC_FAILED, e);
		} finally {
			try {
				output.flush();
			} catch (IOException e) {
				logger.error("Executed command errors: \n", error);
				throw new YpException(YpError.EXEC_FAILED, e);
			}
		}

		// in case of errrors => log them
		if (0 != exitValue) {
			logger.error("Executed command return value: \n", exitValue);
			if (null != error && !StringUtils.isEmpty(error.toString())) {
				logger.error("Executed command errors: " + error.toString());
			}
		} else {
			logger.debug("Executed command return value: " + exitValue);
			// quiet flag introduced to prevent too much stuff in logs
			if (!quiet && null != error
					&& !StringUtils.isEmpty(error.toString())) {
				logger.debug("Executed command errors: ", error.toString());
			}
		}

		return exitValue;
	}

	public static OS getOS() {
		String os = System.getProperty("os.name").toLowerCase();

		if (os.contains("win")) {
			return OS.WINDOWS;
		} else if (os.contains("nix")) {
			return OS.LINUX;
		} else if (os.contains("nux")) {
			return OS.UNIX;
		} else if (os.contains("mac")) {
			return OS.MAC;
		} else {
			return OS.UNKNOWN;
		}
	}
}
