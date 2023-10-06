package com.unifiedpost.btx.flowable.extensions.storage;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.flowable.content.engine.impl.AbstractContentStorage;
import com.flowable.content.engine.impl.ContentItemContentObjectStorageMetadata;
import com.google.cloud.storage.Storage;
import org.flowable.content.api.ContentItem;
import org.flowable.content.api.ContentObject;
import org.flowable.content.api.ContentObjectStorageMetadata;
import org.flowable.content.api.ContentStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Map;

public class GcpContentStorage extends AbstractContentStorage implements ContentStorage {

    Logger logger = LoggerFactory.getLogger(GcpContentStorage.class);

    private static final TimeBasedGenerator UUID_GENERATOR = Generators.timeBasedGenerator(EthernetAddress.fromInterface());
    protected final Storage storage;
    protected final String bucket;

    public GcpContentStorage(Storage storage, String bucket) {
        this.storage = storage;
        this.bucket = bucket;
    }

    @Override
    public ContentObject createContentObject(InputStream contentStream, ContentObjectStorageMetadata metaData) {
        String contentId = generateContentId(UUID_GENERATOR.generate().toString(), metaData);
        logger.info("Creating content object with id {}", contentId);
        logger.info("Metadata = {}", inspectMetaData(metaData));
        return uploadFile(contentStream, contentId, metaData);
    }

    @Override
    public ContentObject updateContentObject(String contentId, InputStream contentStream, ContentObjectStorageMetadata metaData) {
        logger.info("Updating content object with id {}", contentId);
        logger.info("Metadata = {}", inspectMetaData(metaData));
        return uploadFile(contentStream, contentId, metaData);
    }

    @Override
    public ContentObject getContentObject(String contentId) {
        logger.info("Get content object with id {}", contentId);
        return getGcpContentObject(contentId);
    }

    @Override
    public Map<String, Object> getMetaData() {
        logger.info("GetMetaData called");
        return null;
    }

    @Override
    public void deleteContentObject(String contentId) {
        logger.info("Deleting content object {}", contentId);
        // StorageClient: delete object
    }

    @Override
    public String getContentStoreName() {
        return "gcp-cloud-storage";
    }

    protected ContentObject uploadFile(InputStream contentStream, String contentId, ContentObjectStorageMetadata metadata) {
        logger.info("Uploading the content with id {} to storage", contentId);
        // StorageClient: construct upload request, if necessary taking into account the metadata & upload
        return getGcpContentObject(contentId);
    }

    protected GcpContentObject getGcpContentObject(String contentId) {
        return new GcpContentObject(contentId, this.bucket);
    }

    private String inspectMetaData(ContentObjectStorageMetadata metaData) {
        StringBuilder output = new StringBuilder();
        output
                .append("Metadata type = ").append(metaData.getClass().getName()).append("\n")
                .append("Metadata type = ").append(metaData.getClass().getTypeName()).append("\n")
                .append("Metadata type = ").append(metaData.getClass().getCanonicalName()).append("\n")
                .append("Metadata content name = ").append(metaData.getName()).append("\n")
                .append("Metadata content scope id = ").append(metaData.getScopeId()).append("\n")
                .append("Metadata content scope type = ").append(metaData.getScopeType()).append("\n")
                .append("Metadata content mime type = ").append(metaData.getMimeType()).append("\n")
                .append("Metadata content tenant id = ").append(metaData.getTenantId()).append("\n");
        if (metaData instanceof ContentItemContentObjectStorageMetadata) {
            ContentItem contentItem = (ContentItem) ((ContentItemContentObjectStorageMetadata) metaData).getStoredObject();
            output
                    .append("Content Item id = ").append(contentItem.getId()).append("\n")
                    .append("Content Item name = ").append(contentItem.getName()).append("\n")
                    .append("Content Item mimeType = ").append(contentItem.getMimeType()).append("\n")
                    .append("Content Item task id = ").append(contentItem.getTaskId()).append("\n")
                    .append("Content Item process instance id = ").append(contentItem.getProcessInstanceId()).append("\n")
                    .append("Content Item scope id = ").append(contentItem.getScopeId()).append("\n")
                    .append("Content Item content store id = ").append(contentItem.getContentStoreId()).append("\n")
                    .append("Content Item content store name = ").append(contentItem.getContentStoreName()).append("\n")
                    .append("Content Item - is content available = ").append(contentItem.isContentAvailable()).append("\n")
                    .append("Content Item - is provisional = ").append(contentItem.isProvisional()).append("\n")
                    .append("Content Item content size = ").append(contentItem.getContentSize()).append("\n")
                    .append("Content Item creation date = ").append(contentItem.getCreated()).append("\n")
                    .append("Content Item creator = ").append(contentItem.getCreatedBy()).append("\n")
                    .append("Content Item last modification date = ").append(contentItem.getLastModified()).append("\n")
                    .append("Content Item last modified by = ").append(contentItem.getLastModifiedBy()).append("\n");
        }
        return output.toString();
    }
}
