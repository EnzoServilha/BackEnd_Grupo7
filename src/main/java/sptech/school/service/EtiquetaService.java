package sptech.school.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.stereotype.Service;
import sptech.school.entity.Item;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class EtiquetaService {

    private final ItemService itemService;

    public EtiquetaService(ItemService itemService) {
        this.itemService = itemService;
    }

    public byte[] gerarEtiquetaPdf(Integer itemId) {
        Item item = itemService.buscarPorId(itemId);

        try (InputStream jrxmlStream = getClass().getResourceAsStream("/etiqueta.jrxml")) {
            if (jrxmlStream == null) {
                throw new RuntimeException("Template 'etiqueta.jrxml' não encontrado no classpath.");
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("codigoInterno", item.getCodigoInterno() != null ? item.getCodigoInterno() : "");
            parameters.put("marca", item.getMarca() != null ? item.getMarca() : "");
            parameters.put("descricao", item.getDescricao() != null ? item.getDescricao() : "");
            parameters.put("localizacao", item.getLocalizacao() != null ? item.getLocalizacao() : "");

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport, parameters, new JREmptyDataSource());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
            exporter.exportReport();

            return outputStream.toByteArray();

        } catch (JRException e) {
            throw new RuntimeException("Erro ao gerar etiqueta PDF: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao gerar etiqueta: " + e.getMessage(), e);
        }
    }
}
