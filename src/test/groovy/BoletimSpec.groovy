import spock.lang.Specification
import spock.lang.Unroll

class BoletimSpec extends Specification {
    private final Class boletimClass = new GroovyClassLoader().parseClass(
        new File('src/main/groovy/Boletim.groovy')
    )

    def "calcularMedia calcula a média das três notas"() {
        expect:
        Math.abs(boletimClass.newInstance().calcularMedia(notas) - media) < 0.0000001

        where:
        notas             | media
        [8.0, 7.5, 9.0]   | 8.166666666666667
        [5.0, 6.0, 7.0]   | 6.0
        [10.0, 4.0, 7.0]  | 7.0
        [3.0, 9.0, 9.0]   | 7.0
    }

    @Unroll
    def "situacao classifica a média #media como #resultado"() {
        expect:
        boletimClass.newInstance().situacao(media, 7.0, 5.0) == resultado

        where:
        media | resultado
        7.0   | 'Aprovado'
        10.0  | 'Aprovado'
        5.0   | 'Recuperação'
        6.99  | 'Recuperação'
        4.99  | 'Reprovado'
        0.0   | 'Reprovado'
    }

    def "run imprime o boletim esperado para todos os alunos"() {
        given:
        def output = new ByteArrayOutputStream()
        def originalOut = System.out
        System.setOut(new PrintStream(output, true, 'UTF-8'))

        when:
        boletimClass.newInstance().run()

        then:
        output.toString('UTF-8') == '''=== Boletim da turma ===
Ana: média 8.17 - Aprovado
Bruno: média 6.00 - Recuperação
Carla: média 7.00 - Aprovado
Diego: média 7.00 - Aprovado
'''

        cleanup:
        System.setOut(originalOut)
    }
}
