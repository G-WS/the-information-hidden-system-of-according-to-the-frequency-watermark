import numpy as np
import cv2 as cv


def text_read(filename):
    try:
        file = open(filename, 'r')
    except IOError:
        error = []
        return error
    content = file.readlines()
    for i in range(len(content)):
        content[i] = int(content[i][:len(content[i]) - 1])
    file.close()
    return content


# src1 = original, src2 = watermarked image, key1 = m.txt, key2 = n.txt, dst = extracted watermark
def decrypt(src1, src2, key1, key2, dst):
    alpha = 1

    img = cv.imread(src1) / 255.0
    wmi = cv.imread(src2) / 255.0

    img_size = np.shape(img)
    m = text_read(key1)
    n = text_read(key2)

    fa2 = np.fft.fft2(wmi)
    fa = np.fft.fft2(img)
    g = (fa2 - fa) / alpha
    gg = g.copy()
    for i in range(0, int(img_size[0] * 0.5)):
        for j in range(0, img_size[1]):
            gg[m[i], n[j], :] = g[i, j, :]
    for i in range(0, int(img_size[0] * 0.5)):
        for j in range(0, img_size[1]):
            gg[img_size[0] - 1 - i, img_size[1] - 1 - j, :] = gg[i, j, :]
    cv.imwrite(dst, np.abs(gg) * 255.0, [int(cv.IMWRITE_PNG_COMPRESSION), 9])


#decrypt('nanami.png', 'watermarked_image.png', 'm.txt', 'n.txt', 'extracted_watermark.png')
