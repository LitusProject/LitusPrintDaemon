/**
 * Litus is a project by a group of students from the KU Leuven. The goal is to create
 * various applications to support the IT needs of student unions.
 *
 * @author Niels Avonds <niels.avonds@litus.cc>
 * @author Karsten Daemen <karsten.daemen@litus.cc>
 * @author Koen Certyn <koen.certyn@litus.cc>
 * @author Bram Gotink <bram.gotink@litus.cc>
 * @author Dario Incalza <dario.incalza@litus.cc>
 * @author Pieter Maene <pieter.maene@litus.cc>
 * @author Kristof Mariën <kristof.marien@litus.cc>
 * @author Lars Vierbergen <lars.vierbergen@litus.cc>
 * @author Daan Wendelen <daan.wendelen@litus.cc>
 *
 * @license http://litus.cc/LICENSE
 */

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.common.card.containers.JobStatusInfo;
import com.zebra.sdk.common.card.containers.TemplateJob;
import com.zebra.sdk.common.card.enumerations.CardDestination;
import com.zebra.sdk.common.card.errors.ZebraCardErrors;
import com.zebra.sdk.common.card.exceptions.ZebraCardException;
import com.zebra.sdk.common.card.jobSettings.ZebraCardJobSettingNames;
import com.zebra.sdk.common.card.printer.ZebraCardPrinter;
import com.zebra.sdk.common.card.printer.ZebraCardPrinterFactory;
import com.zebra.sdk.common.card.template.ZebraCardTemplate;
import com.zebra.sdk.printer.discovery.DiscoveredUsbPrinter;
import com.zebra.sdk.settings.SettingsException;

public class TemplateModel {

	private static final long CARD_FEED_TIMEOUT = 30000;
	private final String templateFilePath;
	private final String templateName;

	private final ZebraCardTemplate zebraCardTemplate;

	public TemplateModel(String templateFilePath) throws IOException, IllegalArgumentException, ZebraCardException {
		super();

		this.templateFilePath = templateFilePath;

		zebraCardTemplate = new ZebraCardTemplate(null);

		String templateFileName = new File(templateFilePath).getName();
		templateName = removeFileExtension(templateFileName);
		saveTemplateFile(templateFilePath);
	}

	private void saveTemplateFile(String templateFilePath) throws IOException, IllegalArgumentException, ZebraCardException {
		if (templateFilePath != null) {
			if (templateName == null) {
				throw new IllegalArgumentException("No template name was found for " + this.templateFilePath + ".");
			}

			String templateData = FileUtils.readFileToString(new File(templateFilePath));

			List<String> storedTemplateNames = zebraCardTemplate.getAllTemplateNames();
			if (storedTemplateNames.contains(templateName)) {
				zebraCardTemplate.deleteTemplate(templateName);
			}

			zebraCardTemplate.saveTemplate(templateName, templateData);
		} else {
			throw new IllegalArgumentException("Must specify a template or image file path");
		}
	}

	public List<String> getTemplateFields() throws IllegalArgumentException, ZebraCardException, IOException {
		return zebraCardTemplate.getTemplateFields(templateName);
	}

	private String removeFileExtension(String fileName) {
		return fileName.split("\\.")[0];
	}

	public void print(DiscoveredUsbPrinter discoveredPrinter, Map<String, String> variableData)
			throws IllegalArgumentException, IOException, ConnectionException, SettingsException, ZebraCardException {
		ZebraCardPrinter zebraCardPrinter = null;
		Connection connection = null;

		try {
			String templateData = zebraCardTemplate.getTemplate(templateName);
			TemplateJob templateJob = generateTemplateJob(templateName, zebraCardTemplate, templateData, variableData);

			connection = discoveredPrinter.getConnection();
			connection.open();

			zebraCardPrinter = ZebraCardPrinterFactory.getInstance(connection);

			boolean isDestinationValid = zebraCardPrinter.isJobSettingValid(ZebraCardJobSettingNames.CARD_DESTINATION, CardDestination.Eject.name());
			if (!isDestinationValid) {
				zebraCardPrinter.setJobSetting(ZebraCardJobSettingNames.CARD_DESTINATION, CardDestination.LaminatorAny.name());
			}

			int jobId = zebraCardPrinter.printTemplate(1, templateJob);
			System.out.println("[" + (new Date()).toString() + "]: Started a new card template job with id : " + jobId);
			pollJobStatus(zebraCardPrinter, jobId);
		} finally {
			cleanUpQuietly(zebraCardPrinter, connection);
		}
	}

	private TemplateJob generateTemplateJob(String templateFileName, ZebraCardTemplate zebraCardTemplate, String templateData, Map<String, String> fieldDataMap)
			throws ConnectionException, SettingsException, ZebraCardException, IOException {
		if (templateFileName != null) {
			return zebraCardTemplate.generateTemplateJob(templateFileName, fieldDataMap);
		} else {
			throw new IllegalArgumentException("Must specify a template file name or template data");
		}
	}

    private void pollJobStatus(ZebraCardPrinter zebraCardPrinter, int jobId) throws ConnectionException, ZebraCardException {
        boolean done = false;
        long start = System.currentTimeMillis();

        System.out.println("Polling status for job id " + jobId + "...\n");

        while (!done) {
            JobStatusInfo jobStatus = zebraCardPrinter.getJobStatus(jobId);

            String alarmDesc = jobStatus.alarmInfo.value > 0 ? " (" + jobStatus.alarmInfo.description + ")" : "";
            String errorDesc = jobStatus.errorInfo.value > 0 ? " (" + jobStatus.errorInfo.description + ")" : "";

            System.out.println(String.format("Job %d: status:%s, position:%s, contact:%s, contactless:%s, alarm:%d%s, error:%d%s%n", jobId, jobStatus.printStatus, jobStatus.cardPosition, jobStatus.contactSmartCard, jobStatus.contactlessSmartCard, jobStatus.alarmInfo.value, alarmDesc, jobStatus.errorInfo.value, errorDesc));

            if (jobStatus.printStatus.contains("done_ok")) {
                done = true;
            } else if (jobStatus.printStatus.contains("error") || jobStatus.printStatus.contains("cancelled")) {
                if (jobStatus.errorInfo.value > 0) {
                    System.out.println("An error occurred");
                } else {
                    System.out.println("Job ID " + jobId + " was cancelled.");
                }
                done = true;
            } else if (jobStatus.alarmInfo.value > 0) {
                System.out.println("Warning: " + jobStatus.alarmInfo.description);
            } else if (jobStatus.errorInfo.value > 0) {
                zebraCardPrinter.cancel(jobId);
                System.out.println("An error occurred");
                done = true;
            } else if ((jobStatus.printStatus.contains("in_progress") && jobStatus.cardPosition.contains("feeding")) // ZMotif printers
                    || (jobStatus.printStatus.contains("alarm_handling") && jobStatus.alarmInfo.value == ZebraCardErrors.MEDIA_OUT_OF_CARDS)) { // ZXP printers
                if (System.currentTimeMillis() > start + CARD_FEED_TIMEOUT) {
                    zebraCardPrinter.cancel(jobId);
                    System.out.println("Job ID " + jobId + " was cancelled.");
                    done = true;
                }
            }

            if (!done) {
                sleep(500);
            }
        }
    }
	public static void cleanUpQuietly(ZebraCardPrinter zebraCardPrinter, Connection connection) {
		try {
			if (zebraCardPrinter != null) {
				zebraCardPrinter.destroy();
			}
		} catch (ZebraCardException e) {
		}

		try {
			if (connection != null) {
				connection.close();
			}
		} catch (ConnectionException e) {
		}
	}
	
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
}
