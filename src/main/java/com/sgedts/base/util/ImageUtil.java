package com.sgedts.base.util;

import com.sgedts.base.constant.ContentTypeConstant;
import com.sgedts.base.constant.StorageConstant;
import com.sgedts.base.constant.enums.MessageResourceEnum;
import com.sgedts.base.exception.BadRequestException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.Set;

/**
 * Collections of image utility functions.
 */
public final class ImageUtil {
    private static final Set<String> validImageContentType = Set.of(ContentTypeConstant.JPEG, ContentTypeConstant.PNG);

    public static String generateFilePath(long orderMinDeliveryTime, String orderId, String fileName, String imagePrefix) {
        Date date = new Date(orderMinDeliveryTime);
        String dateFormat = DateFormatUtils.format(date, "yyyyMM");
        String destinationFileName = StringUtils.joinWith("_", orderId, imagePrefix, fileName);
        return StringUtils.joinWith("/", StorageConstant.UPLOADED_IMAGE_FOLDER, dateFormat, orderId, destinationFileName);
    }

    public static String getExtensionName(String contentType) {
        if (StringUtils.isBlank(contentType)) {
            return "";
        }
        switch (contentType) {
            case ContentTypeConstant.JPEG:
                return ".jpg";
            case ContentTypeConstant.PNG:
                return ".png";
            default:
                return "";
        }
    }

    public static void checkIsValidImageContentType(String contentType) {
        if (!validImageContentType.contains(contentType)) {
            throw new BadRequestException(MessageResourceEnum.INVALID_FILE);
        }
    }
}
