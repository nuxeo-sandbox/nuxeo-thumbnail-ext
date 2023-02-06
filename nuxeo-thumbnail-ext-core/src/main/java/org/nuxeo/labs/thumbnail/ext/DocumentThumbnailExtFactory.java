/*
 * (C) Copyright 2021 Nuxeo (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Michael Vachette
 */

package org.nuxeo.labs.thumbnail.ext;

import static org.nuxeo.ecm.platform.thumbnail.ThumbnailConstants.ANY_TO_THUMBNAIL_CONVERTER_NAME;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.Blobs;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.blobholder.SimpleBlobHolder;
import org.nuxeo.ecm.core.convert.api.ConversionService;
import org.nuxeo.ecm.platform.mimetype.interfaces.MimetypeRegistry;
import org.nuxeo.ecm.platform.thumbnail.factories.ThumbnailDocumentFactory;
import org.nuxeo.ecm.platform.types.adapter.TypeInfo;
import org.nuxeo.runtime.api.Framework;

public class DocumentThumbnailExtFactory extends ThumbnailDocumentFactory {

    private static final Logger log = LogManager.getLogger(DocumentThumbnailExtFactory.class);

    public static final String THUMBNAIL_FORMAT = "image/png";

    @Override
    public Blob computeThumbnail(DocumentModel doc, CoreSession session) {
        ConversionService conversionService = Framework.getService(ConversionService.class);
        BlobHolder bh = doc.getAdapter(BlobHolder.class);
        if (bh != null && bh.getBlob() != null) {
            Blob blob = bh.getBlob();
            List<String> converterNames = conversionService.getConverterNames(blob.getMimeType(), THUMBNAIL_FORMAT);
            if (converterNames.contains(ANY_TO_THUMBNAIL_CONVERTER_NAME)) {
                return super.computeThumbnail(doc, session);
            } else if (!converterNames.isEmpty()) {
                BlobHolder converted =  conversionService.convert(converterNames.get(0),new SimpleBlobHolder(blob),new HashMap<>());
                return converted.getBlob();
            } else {
                log.warn(String.format("%s converter doesn't support %s", ANY_TO_THUMBNAIL_CONVERTER_NAME, blob.getMimeType()));
            }
        }
        return null;
    }

    @Override
    protected Blob getDefaultThumbnail(DocumentModel doc) {
        if (doc != null) {
            String iconPath;
            // get document instance icon
            if (doc.hasSchema("common") && StringUtils.isNotBlank((String) doc.getPropertyValue("common:icon"))) {
                iconPath = (String) doc.getPropertyValue("common:icon");
            } else {
                //get document type icon
                TypeInfo docType = doc.getAdapter(TypeInfo.class);
                iconPath = docType.getBigIcon();
                if (iconPath == null) {
                    iconPath = docType.getIcon();
                }
            }
            if (iconPath != null) {
                try {
                    File iconFile = FileUtils.getResourceFileFromContext("nuxeo.war" + File.separator + iconPath);
                    if (iconFile!= null && iconFile.exists()) {
                        MimetypeRegistry mimetypeRegistry = Framework.getService(MimetypeRegistry.class);
                        String mimeType = mimetypeRegistry.getMimetypeFromFile(iconFile);
                        if (mimeType == null) {
                            mimeType = mimetypeRegistry.getMimetypeFromFilename(iconPath);
                        }
                        return Blobs.createBlob(iconFile, mimeType);
                    }
                } catch (IOException e) {
                    log.warn(String.format("Could not fetch the thumbnail blob from icon path '%s'", iconPath), e);
                }
            }
        }
        return null;
    }
}
