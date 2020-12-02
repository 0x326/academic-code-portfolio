#lang scheme

(require 2htdp/image)

; Cube primitives

(define (cube-side size mode color)
  (rhombus size (- 90 30) mode color))

(define (cube-left-side size mode color)
  (rotate 30
          (cube-side size mode color)))

(define (cube-right-side size mode color)
  (rotate -30
          (cube-side size mode color)))

(define (cube-top-side size mode color)
  (rotate 90
          (cube-side size mode color)))

(define (cube size mode color)
  (let ([top-color (list-ref color 0)]
        [left-color (list-ref color 1)]
        [right-color (list-ref color 2)])
    (overlay/align/offset
     "middle" "middle"
     (cube-top-side size mode top-color)
     0 (/ (* 3 size) 4)
     (beside
      (cube-left-side size mode left-color)
      (cube-right-side size mode right-color)))))

; Composing cubes

(define (cube-row-helper cube row-length partial-image)
  (cond
    [(= row-length 0) partial-image]
    [else (cube-row-helper cube (- row-length 1) (beside partial-image (cube row-length)))]))

(define (cube-row cube row-length)
  (cube-row-helper cube row-length empty-image))

(define (cube-stack-helper cube-row size cube-size partial-image)
  (let ([row-overlay-y-offset (- (/ (* 3 cube-size) 2))]
        [row-overlay-x-offset (if (even? size)
                                  0
                                  (* (/ (sqrt 3) 2) cube-size))])
    (cond
      [(<= size 0) partial-image]
      [else
       (cube-stack-helper cube-row (- size 1) cube-size
                          (overlay/align/offset "left" "top" partial-image
                                                row-overlay-x-offset
                                                row-overlay-y-offset
                                                (cube-row size)))])))

(define (cube-stack cube-row size cube-size)
  (cube-stack-helper cube-row size cube-size empty-image))

; Color compositions

(define (colored-cube-stack size cube-size color)
  (cube-stack
   (lambda (row-length)
     (cube-row
      (lambda (column-length)
        (cube cube-size "solid" (color size row-length column-length)))
      size))
   size
   cube-size))

; Define higher-lever functions to choose colors

(define (distance x y x_0 y_0)
  (sqrt (+ (expt (- x x_0) 2) (expt (- y y_0) 2))))

(define (color-circle colors)
  (lambda (size row-length column-length)
    (let ([distance-from-center (distance column-length row-length (/ size 2) (/ size 2))])
      (list-ref colors (modulo (exact-ceiling (/ distance-from-center 2.5)) (length colors))))))

(define (color-sine colors)
  (lambda (size row-length column-length)
    (let ([half-length (exact-round (/ (length colors) 2))])
      (list-ref colors (exact-round (+ (* (sin (+ row-length column-length)) half-length) half-length))))))

(define (color-vertical colors)
  (lambda (size row-length column-length)
    (list-ref colors (modulo (+ (* row-length size) column-length) (length colors)))))

(define (color-diagonal colors)
  (lambda (size row-length column-length)
    (list-ref colors (modulo (+ row-length column-length) (length colors)))))

; Define colors

(define colors '(("cornflowerblue" "royalblue" "steelblue")
                 ("tomato" "darkred" "Firebrick")
                 ("VioletRed" "DarkRed" "Maroon")
                 ("Green Yellow" "MediumForestGreen" "Yellow Green")
                 ("Purple" "DarkMagenta" "BlueViolet")))

; Generate images

(colored-cube-stack 10 40 (color-sine colors))

(save-image (colored-cube-stack 50 15 (color-circle colors)) "2019-02-11 Scheme Art.png")
(save-svg-image (colored-cube-stack 50 15 (color-circle colors)) "2019-02-11 Scheme Art.svg")
