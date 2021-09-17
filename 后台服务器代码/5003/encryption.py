import numpy as np
import cv2 as cv


def text_save(content, filename, mode='a'):
    file = open(filename, mode)
    for i in range(len(content)):
        file.write(str(content[i]) + '\n')
    file.close()


# src1 = original, src2 = watermark, key1 = m.txt, key2 = n.txt, dest1 = watermarked img, dst2 = expected watermark
def encrypt(src1, src2, key1, key2, dst1, dst2):
    alpha = 1
    # read data
    img = cv.imread(src1) / 255.0
    wm = cv.imread(src2) / 255.0
    # encode mark
    img_size = np.shape(img)
    # random
    th = np.zeros((int(img_size[0] * 0.5), img_size[1], img_size[2]))
    th1 = th.copy()
    th1[0: np.size(wm, 0), 0: np.size(wm, 1), :] = wm[0: min(int(img_size[0] * 0.5), np.size(wm, 0)),
                                                   0: min(img_size[1], np.size(wm, 1)), :]
    m = [i for i in range(0, int(img_size[0] * 0.5))]
    np.random.shuffle(m)
    n = [i for i in range(0, img_size[1])]
    np.random.shuffle(n)
    text_save(m, key1, 'w')
    text_save(n, key2, 'w')
    for i in range(0, int(img_size[0] * 0.5)):
        for j in range(0, img_size[1]):
            th[i, j, :] = th1[m[i], n[j], :]
    # symmetry
    wm1 = np.zeros((img_size[0], img_size[1], img_size[2]))
    wm1[0: int(img_size[0] * 0.5), 0: img_size[1], :] = th
    for i in range(0, int(img_size[0] * 0.5)):
        for j in range(0, img_size[1]):
            wm1[img_size[0] - 1 - i, img_size[1] - 1 - j, :] = th[i, j, :]
    # add watermark
    fa = np.fft.fft2(img)
    fb = fa + alpha * wm1
    fao = np.fft.ifft2(fb)
    cv.imwrite(dst1, np.abs(fao) * 255.0, [int(cv.IMWRITE_PNG_COMPRESSION), 9])

    fa2 = np.fft.fft2(fao)
    g = (fa2 - fa) / alpha
    gg = g.copy()
    for i in range(0, int(img_size[0] * 0.5)):
        for j in range(0, img_size[1]):
            gg[m[i], n[j], :] = g[i, j, :]
    for i in range(0, int(img_size[0] * 0.5)):
        for j in range(0, img_size[1]):
            gg[img_size[0] - 1 - i, img_size[1] - 1 - j, :] = gg[i, j, :]
    cv.imwrite(dst2, np.abs(gg) * 255.0, [int(cv.IMWRITE_PNG_COMPRESSION), 9])


#encrypt('nanami.png', '010.png', 'm.txt', 'n.txt', 'watermarked_image.png', 'expected watermark.png')
