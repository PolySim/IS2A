#!/bin/python3

# F. Boulier 2023

import sympy

# Auxiliary function for Python code generation
# P   : the expression for which code must be generated
# D0  : the dictionary of already computed expressions
# n0  : the number of the first free temporary variable
# tab : if True, generated expressions start with a four spaces tabulation

def Python_aux (P, D0, n0, tab, mathlib) :
    n = n0
    D = D0
    if P in D :
        result = D[P]
    elif isinstance (P, sympy.core.symbol.Symbol) :
        result = P
    elif isinstance (P, sympy.core.Integer) or \
                            isinstance (P, sympy.core.Rational) :
        if P < 0 :
            result = '(%s)' % str (P)
        else :
            result = P
    elif isinstance (P, sympy.core.Add) :
        result = 't%d' % n
        n += 1
        L = []
        for arg in P.args :
            tmp, D, n = Python_aux (arg, D, n, tab, mathlib)
            L.append (tmp)
        if (tab) :
            print (' ' * 4, end ='')
        print (result, '=', end=' ')
        for i in range (0, len(L)-1) :
            print (L[i], '+', end=' ')
        print (L[-1])
    elif isinstance (P, sympy.core.Mul) :
        result = 't%d' % n
        n += 1
        L = []
        for arg in P.args :
            tmp, D, n = Python_aux (arg, D, n, tab, mathlib)
            L.append (tmp)
        if (tab) :
            print (' ' * 4, end ='')
        print (result, '=', end=' ')
        for i in range (0, len(L)-1) :
            print (L[i], '*', end=' ')
        print (L[-1])
    elif isinstance (P, sympy.core.Pow) :
        result = 't%d' % n
        n += 1
        tmp, D, n = Python_aux (P.args[0], D, n, tab, mathlib)
        if (tab) :
            print (' ' * 4, end ='')
        if P.args [1] == sympy.core.Rational(1,2) :
            print (result, ' = %s.sqrt (' % mathlib, tmp, ')', sep='')
        elif P.args[1] == sympy.core.Rational (-1,2) :
            print (result, ' = 1. / %s.sqrt (' % mathlib, tmp, ')', sep='')
        elif isinstance (P.args[1], sympy.core.Integer) and P.args[1] > 0 :
            print (result, ' = ', tmp, '**', P.args[1], sep='')
        else :
            print (result, ' = ', tmp, '**(', P.args[1], ')', sep='')
    elif isinstance (P, (sympy.sin, sympy.asin, sympy.cos, sympy.acos, \
                         sympy.exp, sympy.log, sympy.tan, sympy.atan,  \
                         sympy.sinh, sympy.asinh, sympy.cosh, sympy.acosh, \
                         sympy.tanh, sympy.atanh, sympy.sqrt)) :
        result = 't%d' % n
        n += 1
        tmp, D, n = Python_aux (P.args[0], D, n, tab, mathlib)
        if (tab) :
            print (' ' * 4, end ='')
        print (result, ' = %s.%s (' % (mathlib, P.func.__name__), tmp, ')', sep='')
    D[P] = result
    return result, D, n

# Function for Python code generation in Python
# P       : the expression for which code must be generated
# fname   : if not None, the code is generated as a function with
#           name fname and argument list argname (default None)
# argname : the argument list (default 'x')
# mathlib : the library name for functions such as sin, exp, atan, ...

def Python (P, fname=None, argname='x', mathlib='math') :
    if not fname is None :
        print ('def %s (%s) :' % (str(fname), str(argname)))
        Python_aux (P, dict (), 0, True, mathlib)
        print (' ' * 4, 'return t0', sep='')
    else :
        Python_aux (P, dict (), 0, False, mathlib)

