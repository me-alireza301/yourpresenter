package com.google.code.yourpresenter.media;

import java.io.IOException;

import name.pachler.nio.file.ClosedWatchServiceException;
import name.pachler.nio.file.FileSystems;
import name.pachler.nio.file.Path;
import name.pachler.nio.file.Paths;
import name.pachler.nio.file.StandardWatchEventKind;
import name.pachler.nio.file.WatchEvent;
import name.pachler.nio.file.WatchEvent.Kind;
import name.pachler.nio.file.WatchKey;
import name.pachler.nio.file.WatchService;

public class MediaWatcher {

	private String[] getPaths() {
		return new String[] { "D:/test_imgs/1", "D:/test_imgs/2" };
	}

	/**
	 * Watch the specified directory
	 * 
	 * @param dirname
	 *            the directory to watch
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void watchDir(String dir) {

		// create the watchService
		final WatchService watchService = FileSystems.getDefault()
				.newWatchService();

		// register the directory with the watchService
		// for create, modify and delete events
		final Path path = Paths.get(dir);
		try {
			path.register(watchService, StandardWatchEventKind.ENTRY_CREATE,
					StandardWatchEventKind.ENTRY_MODIFY,
					StandardWatchEventKind.ENTRY_DELETE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// start an infinite loop
		while (true) {

			// remove the next watch key
			WatchKey key = null;
			try {
				key = watchService.take();
			} catch (ClosedWatchServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// get list of events for the watch key
			for (WatchEvent<?> watchEvent : key.pollEvents()) {

				// get the filename for the event
				final WatchEvent<Path> ev = (WatchEvent<Path>) watchEvent;
				final Path filename = ev.context();

				// get the kind of event (create, modify, delete)
				final Kind<?> kind = watchEvent.kind();

				// print it out
				System.out.println(kind + ": " + filename);
			}

			// reset the key
			boolean valid = key.reset();

			// exit loop if the key is not valid
			// e.g. if the directory was deleted
			if (!valid) {
				break;
			}
		}
	}
}
