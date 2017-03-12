package com.none.mobilemaths

@Singleton
class MathPack {

    def transpose(matrix)
    {
        def transposed = [[]]*matrix[0].size()
        matrix[0].eachWithIndex(){num,index -> (matrix.size()).times(){transposed[index]+=matrix[it][index]}}
        return transposed
    }

    def minify(fdel, sdel, matrix)
    {
        def ret = matrix.collect()
        ret.remove(fdel); ret = transpose(ret)
        ret.remove(sdel); ret = transpose(ret);
        return ret
    }

    def determinant(matrix)
    {
        if (matrix.size()==1 && matrix[0].size()==1) return matrix[0][0]
        def result = 0
        if (matrix.size()==2 && matrix[0].size()==2)
        {
            result = matrix[0][0]*matrix[1][1]-matrix[0][1]*matrix[1][0]
        }
        else
        {
            (matrix[0].size()).times() {if (it%2==0) result+=((matrix[0][it])*determinant(minify(0, it, matrix)))
            else result+=(-((matrix[0][it]))*determinant(minify(0,it,matrix)))
            }
        }
        return result
    }

    def gcd (a,b)
    {
        return a ? gcd(b%a,a) : b
    }

    def factorial(int arg)
    {
        assert arg>=0
        return (arg==0 ? 1 : (1..arg).toList().inject(1) {acc, item -> acc*item})
    }

    def CFormula(int bigger, int less)
    {
        assert bigger >= less
        def div1 = factorial(bigger)
        def div2 = factorial(less)*factorial(bigger-less)
        return div1/div2

    }

    def binome (typ, xarg, yarg, power)
    {
        def binomials = []
        def n = power
        binomials << 1
        (n-1).times {binomials << CFormula(n, it+1)}
        binomials << 1
        def types = ["VV" : 1, "VN" : 2]
        def answer = ""
        for (i in (0..<binomials.size()))
        {
            if (types[typ]==1)
            {
                if (i!=0 && i!=(binomials.size()-1))
                {
                    answer += "${(xarg**(n-i))*(yarg**i)*binomials[i]}x<sup>${n-i}</sup>y<sup>${i}</sup> + "
                }
                else
                {
                    answer += i==0 ? "${xarg**(n-i)}x<sup>${n-i}</sup> + " : "${yarg**i}y<sup>${i}</sup>"
                }
            }
            else
            { //xarg - коэффициент при переменной, yarg - константа
                if (i!=0 && i!=(binomials.size()-1))
                {
                    answer += "${(xarg**(n-i))*(yarg**i)*binomials[i]}x<sup>${n-i}</sup> + "
                }
                else
                {
                    answer += i==0 ? "${xarg**(n-i)}x<sup>${n-i}</sup> + " : "${yarg**i}"
                }
            }
        }
        return answer
    }

    def trace(matrix)
    {
        def res = 0
        (matrix.size()).times {res+=matrix[it][it]}
        return res
    }
}