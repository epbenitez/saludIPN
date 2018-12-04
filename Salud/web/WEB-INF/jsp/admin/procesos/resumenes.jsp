<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<head>
    <title>Procesos</title>

    <!-- this page specific styles -->
    <link rel="stylesheet" type="text/css" href="/css/compiled/wizard.css">

    <link rel="stylesheet" type="text/css" href="/css/compiled/theme_styles.css" />
    <link rel="stylesheet" type="text/css" href="/css/bootstrap/bootstrap.min.css" />

    <!-- bootstrap validator -->
    <script type="text/javascript" src="/bootstrap-validator/js/jquery.min.js"></script>
    <script type="text/javascript" src="/bootstrap-validator/js/bootstrap.min.js"></script>

    <!--    <link rel="stylesheet" type="text/css" href="/css/bootstrap/bootstrap.css" /> -->
    <link rel="stylesheet" href="/bootstrap-validator/css/bootstrapValidator.css"/>

    <!-- Add fancyBox main JS and CSS files -->
    <script type="text/javascript" src="/resources/js/fancybox/source/jquery.fancybox.js?v=2.1.5"></script>
    <link rel="stylesheet" type="text/css" href="/resources/js/fancybox/source/jquery.fancybox.css?v=2.1.5" media="screen" />

    <!-- Add Button helper (this is optional) -->
    <link rel="stylesheet" type="text/css" href="/resources/js/fancybox/source/helpers/jquery.fancybox-buttons.css?v=1.0.5" />
    <script type="text/javascript" src="/resources/js/fancybox/source/helpers/jquery.fancybox-buttons.js?v=1.0.5"></script>

    <!-- Add Thumbnail helper (this is optional) -->
    <link rel="stylesheet" type="text/css" href="/resources/js/fancybox/source/helpers/jquery.fancybox-thumbs.css?v=1.0.7" />
    <script type="text/javascript" src="/resources/js/fancybox/source/helpers/jquery.fancybox-thumbs.js?v=1.0.7"></script>

    <!-- Add Media helper (this is optional) -->
    <script type="text/javascript" src="/resources/js/fancybox/source/helpers/jquery.fancybox-media.js?v=1.0.6"></script>

    <script type="text/javascript" src="/js/pdfmake/pdfmake-browserified.min.js"></script>

    <script type="text/javascript" language="javascript" class="init">
                $(document).ready(function () {
        openPdf();
//                setTimeout(function(){ window.parent.$.fancybox.close(); }, 2000);
        });</script>
    <script>
                function openPdf() {
                var docDefinition = {
                pageOrientation: 'landscape',
                        pageMargins: [40, 110, 40, 40],
                        header: function() {
                        return {
                        columns: [
                        {
                        margin: [25, 10, 0, 0],
                                image: 'data:image/jpeg;base64,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',
                                height: 90,
                                width: 85
                        },
                        {
                        margin: [45, 25, 0, 0],
                                width: 'auto',
                                text: 'INSTITUTO POLITÉCNICO NACIONAL \nSecretaría de Servicios Educativos \nDirección de Servicios Estudiantiles',
                                alignment: 'center'
                        },
                        {
                        margin: [30, 30, 0, 0],
                                width: 'auto',
                                text: 'UNIDAD \nMOVIMIENTO \nPERIODO \nCARÁCTER \nCLASIFICADAS \nFUNDAMENTO LEGAL',
                                fontSize: 7,
                                alignment: 'right'

                        },
                        {
                        margin: [0, 30, 30, 0],
                                width: 'auto',
                                text: '<s:property value="ua"/> \n<s:property value="movimiento"/> \n<s:property value="periodo"/> \nConfidencial \nNúmero de boleta,promedio y CURP \nArtículo 3 fraccón II, artículo 18 fracción II, y 21 de la LFTAIPG \nLineamiento 32, fracción XVI',
                                fontSize: 7,
                                alignment: 'left'

                        },
                        {
                        margin: [0, 10, 0, 0],
                                image: 'data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAMDAwMDAwQEBAQFBQUFBQcHBgYHBwsICQgJCAsRCwwLCwwLEQ8SDw4PEg8bFRMTFRsfGhkaHyYiIiYwLTA+PlQBAwMDAwMDBAQEBAUFBQUFBwcGBgcHCwgJCAkICxELDAsLDAsRDxIPDg8SDxsVExMVGx8aGRofJiIiJjAtMD4+VP/CABEIAI4AjgMBIgACEQEDEQH/xAAdAAACAgMBAQEAAAAAAAAAAAAABQYHAgMECAEJ/9oACAEBAAAAAP1TAAAAAAADkW9iltv27QAAEkUd569/3496AAF2UabNekXL9XHKekAUoN0tD7FK0eWMplX0Aja6ZeeIVcls+dausm4ZlXzKYAL+N1+aFZe7vR3nOrJbdtjpE8wzE+l5B/z7hHqa3Ki3y3b6I+Iu98J0U18w0gnu+UTTGl59eDaGyXtIfhM/MNneO5jNbK4afnfoBvW077jiTuElEef/AELpcRaP+hbKWp+1+YxjNbNqx3VXJ7sz3xfkY9TkFanPbhA6ulPoPbFMVUkfgYRLS3TuEsqwjG9uslwAcsdVtvq3H6xxXTHtAA+RnkcOkHKt3zHMAAMMw5MevIAAAAAAAP/EABoBAQEBAQEBAQAAAAAAAAAAAAAGBwQIAgX/2gAIAQIQAAAAAAAOrqi8217rFvS+XM81+rFfH+ef3K69Hdjma6vddwS2d7V9gAAH/8QAGgEBAAMBAQEAAAAAAAAAAAAAAAQGBwMFCP/aAAgBAxAAAAAAAAgRrjoGXRxltY+s75lddGa8No0Gs00eV3stiqEUOsmCAAAP/8QALRAAAgMAAQIGAgEDBQEAAAAAAwQBAgUGABEHEBITFCEVIAgwMUAWIjY3UWH/2gAIAQEAAQwA/wAg+giqUQTtAER3dqroVzwKGbcV0RtOOqSOwik2KNqGtaphic3M9Nn45LluZR5R5T5K5PcGucbK4jDi3o/pYmk3rgh2RhGpkoObCmgIg1A9V+Mlu41BlsaOQg0USB189ax2WMy6KfHc8VblpxyRfI263mIc3nfQdROrFl4BqcgZDVwGUGF0HgaCQWgTM0/d/TXz7CpahjGzdRXVBcoYLSdzHyhTb4OUf8hfAUPajB7Hq0omqgCoFg0CLyfyMjTtWzqKzFtfAG6lrQCYqwXfalewgZL3zxD1Mr8ViJGW93PMywrQjK3xjfq/sp5bIxO1KAPJYdQfG8K/trIamfeU87AsBgHefrr+/U9o6jvPXJOW5PGaUq2SZMXl+7rFkyzPtjzudaKcxXSBDFalDtZk3VYIOiye2bUSYehSK9/20qCb38xNmK2A8k/xtpD8LekqzPbrxG8d8vie21x5QRfm4fK+RaJL6GbyQzd+HeIjevpByNRagmf/AL14u17chV6EQoLwQRLjvjO318glmO0m4N9ceFHUdujYluSLvOniGW+LNJFYdHmRf8d+mnlKai9RsRetlsawm6NNPtPF+uvHX/tnknWdpO47wH0i2Exx6y9+fYtxxHaevFI6j+osdQ0HjrjrMiUaHHXBdjPhAedYvoZ79M/HytdQ9BfbTOy2bVNmMSMYr+4Md/RNfPaZbABcSl4GaUuSB+wbVDdU9Xpj1dvVz/nWT4e4FtbQqUtebbed4jcla3A3BnN6GDs5dh1bROOEG9rHdT1KUgZ9jms8j8PnGBRKzOVnamiz7OYApSt0Kkey2pn2objymSdUsqEJchx3VLFqTNZzTXaz1TXrEW16r2ynJYUq2PNV3Zz1qrM5KK2ArqKLnpotsME8t7PU0c+RtNmUHn6d2WqLo8pSd8v5Uf8AB8jriHgTyZ0eJta4FqZPMqaXg7zxnM47qN/FV2U+R5yGkEFYGhTLf7AtHpLwYIMV7QWoCsRyhA3LOIX0GEKqOcZPcG4nNZ+tWK/fWFPfGQ8sTKDr5Ceg00/dhFEWcGwhXYvHltUeB7BdOFjZ2/q47+Z8ZVpdl3r+VVL24Hl9o+tnS5DqscNvgKgbw/5HxWPE5jrw+GUPDsqhR2HYVpoYdo+pR5Bm5nIBlavAxcp/K8GEH8cyRjMyLe1pJ26bP715+/rB7zi5/eJieh/6SXkpRP7AA5yVER2pQ7B48jmfpc1QqUJ0NTkcTawq4qE5rlHFBlqap7ct4rlc0wHMXTreQZOf4weD2WXPVz1uV5GE+/zLxM1NLkakQ7k5GntlINUVS9fCGN6VGrXUJqZIqCvS9ZtGvxbl+YqOjAmTp5C53tNUCw7FLhcEEP0H05qS3p9H1H9tsZDYugIUGsTO5Rh3UAu9H4wvFLUrgrwP7D5T9x/50pyIGcsNTYIer+QUlLulP2SLhargn2ENK5JL14kpqTnKtyAXyPDrNOpnncvNJptuB0ddxoMzIjli+Wra097JdpTXiYjqi4KXtegqRfprkV6afwFVbNF5DoyULo6JEYXgG+nFaQYOspmZ+MA7R0kKqk89HSjLIpc1YhXk7axVRoAvQmjq5yWoRyyl63aw9QrPdRqJkvKMSd3JIvS0VKDkzfGMOM1vNYCzEdq9ukwPa9AKJiklwDkQB0me/lt7UdqqKFtFsjNnDzSPFA0Zt38pOG58OtdXPQOXT0zPDhgaf6XpUlLUvWLVbWvk6CSeGrnpdM6Sb+pj3VpcWlqYS+hcl/X6Cr6jeMEsOjPYC+vj6wop6qTV7gPHmie5A7rdIIJZgKgVDUQz66C95DBqlPqae5ox7GYtb2x2yeOsBpo6FynS0zVfJnaNKCYDRhLlpwZ/typ+zyKeiD2GQ1JQx8LidjlKs0Kpp0ClGzatg6tNtwmi6mvmGdGrnZ2rdfSUMYV3OGTaKGBcFy63HiWXbu5pDRHQeICtaLna0q21S0zWl85Eqr+WdQjzK5JhlLNKk2xfjjXqbCjnqZoZGsL0V/e1YtE1tETBSDzV3txkBRtY79sfKGmfN0rPYSTSiZLtxWGOtlFozSL6oANXY5INfLo0srFOsuxdDkarY9Kr/QVPhPsoVXgtxx2rH+2K/wBK9KkrEXpFo83VJcDA4OcHSCCuav7K9bREREf2/wAn/8QAOBAAAgIBAwMCBAMECgMAAAAAAQIDERIABCETMUEQYQUiMlEUIHEwUoGhFSNAQmJykZKyszOxtP/aAAgBAQANPwD+0SkCON5FVns0MQavTQCZY1pUwsqWZyaABHPnka2jJkG/vpKLV1rxdj9Qdf0tHs4ZIXAeQrMsZazQC52D/hGinUMMEEs7hboFliVio+xOhkDStkChoqV7gjyO+pUVlyUo1MLFqQCD9wf2e4RW2oDFpSp/vSeFJ/d7jydTbvcw7+eRfxE8rI5GAQgBFC0EuxVcaXZbrYPIzh3zjCSrmR/fpDqKN4HhVbMiSC0/2SUfZb1t97CJZACf/HG7l3/zMOT2JOj8TlM6n6wnAg474mMCtc7reTBipi2u2IZiSKrM0vuL04yjhfcYblkPIJBXBWP7pb9SNTIGWwVIvuCD2Pg/sJiRFBCucj4/UQOAFF8saGo5WilSRCro6d1OtyHaOXaBoAH/AH5JUpVomzZJP2OnjjXcS7eeTbdYoKtxGyg/YHuBpbpEFCz6qCqtJGrMAfAPv51vdvHEMycAIbKJwOEJJyHudEYiCSFhEr/45wDHiPJDE/YaOz3E8u53ETOsjxMgYBFZazaSzzxoZB4sg4tWItWFWrdweLH5pFWt0wHRDE0EZr+Rj4yoHU+2G13G7VS7bRQ2fUVQGsN2LdlNE6RgZ5Y26sUUY5NuCQ0rn3vyfyyqTFEoJLVxZI7DQPCRUQP1JBs68yJ8r/6cA/y1uYWCTRmnTIVYsGiNbKOZerC7lpzKAvKMBgKFkZHn8/4fdTrG3KySpggu+Dirkga3W4ML7CdiIkJRnuFhbR/TyvK+3pDgJ906ZQwl1DClBtjR13cCYSqL8NEbCj+A1IHMM8RIRyoyKlTdGhfp+CH8nbQ7MrUdQyFGagMgRYJA866sv/I+m3306bXaTUYYxtpCoQxmwTKossRfPGhFt3SMilgnfLqwr+7iAtp2Un8qOHhlRikkbjjJCOx1ECITN0wI8hRIWNEBauMjz6dTa/8AzR6gcPG49u4I8g+R500shA9jC3pDCYpinKqQbHPo0qn+Wg7sFYUGBJPH39Pic4gnfIhM442ZGK9ixxxy18OtIoUiVxu9wo6kiPalseQvy0cr0yglT3F80R67vdRQJKy5CPLlmo8EhQa99V23e0V/+kw6oXWjKsUMMf1yyvZCi6A4BJOt3gJNtO5VGMahFIlNrZAA5xGpaET45JIT2wdbD37HW2FokqkjlcSGAI8HQlig3UYPhmFlT5RhoCyIzVAfcmgP46XuQOjJz54tW9uNWDIstBl8cV499KwKkdww5BGpIUZv1YXpIGkMLANnh8wAvzY40YlaKPb7Z5gquM7Vi6LzfetDcSqnVCUEVziylQGOS1eRPrE6zGaORYyhjNglmBoA86JB6U8KNI6KecGhaIcDzifT+nIv+ibW73O2623MzruDDO4VTSiheQ83qNIJhFM2aP1FsrIopW1utushjJvF+VdP0VgQNNwquAQ3sDqWEzF/I6fFc+Pm1tEM0IWQS5JWRphRIZfFDkadjG3uGHp+Fi/4j03MCSzFN9uIVR35ZAkTqqhTxVaZyxM08k7WeKDSsxr29YPiQmcorAiLFwnVUlgRG5Us3tepirbFIZFkcTKbSQBSaCHkt2A9B8aj/wCibXxCRJt/OeHhjXCeB05HBxIOjsNqToJMaZSDi8zsDz9wdK6n+IN6nDQZn6VZzYLexxrW66kb7bcHqLETyqoRRC12GlmT0/CxcHx8o9JZnZ90Jd2u1Z3PLdWunye7XWnfMPNK0rC/sTdD1WFWid5cEdySChIDFaHN0dObbCOTcE/qR0dLaSPg0dyIcHBRuVII7HW5UDJCA8bqcldTzRU6gLnbiKQwbmAHmsCGtSeyrkdCBpTt5IyghaMpGi4P2wXUahnLtSi+Bz76R8XyAdVb9RWiDlfc6SihidpYl98Byv8AEDTSCkXk0O+u4gH0D/MfPpLtZUXpDJwXGNgcWdGMI213aGAA9sUMgAYeBWhLuBtjdgQCZujXsEr8kCiNx0HYzkcdWIIvzB6y47dtfFty8u028pHUxjiVCWUH6jjkyjsNPOwhd0IBKKgYEnHy1K2Kq3Fcn0WcRrNgMwjAkqG70SORrd4FKJsBLHOppMlsUaqtHax2ffEa6Sf+tN9TBQCfTAGgGULlWLl6K9LwTd2CACdfDgJd3NHun20kbKA+MWAt3VSGIJA5GnKgjcARTqrHvkgwk/QgaEhilKwmHMrzYFKGB7hvySSGOacmhCW+gt4Ck8E/cjUskb7ONOXR0YESnyqJVs3auNRKYtzHDIOpi6glQCwWN3pRmRYA1GgZJxys6A4luQDw1qGIAf6l0rK8RPbIffUaukUjUYmJJN5e1+PQRID4VRXdj40qqL+9ek0kkTzpBJPiVRslQRMpLg9wDY5Pg1JCJNxGjGWQn6mVQWovfLY1k3IGviYZjNAipuAkvDlkWhJ8vFim9jqOAQwLIjxGViQ0khRwpAFALY+/5WBBUiwQeDrcJPOw6KxpO0GNREpRUsHJy5IA7aG5aKeFqEqQKpMyy9/kFgqexJBGpUUYtTRkxmlcxt9RTIlQfluiRqCoxmrZhmlwjQSysera8s5PHcnUsYPTlXg2QCAeVaiwBonuNclhE+Kn+BsAfppewA/mfudfKF28bKZGLdqWxxzZJ41NErxTrJgwuwQ6vgUKOoDqCTR1TkMY2whWeSy7Y5CMSMOWY439NDRLvtnSxHuYge6XdOnZ1/jqaET76EmujLISFkSrppK+ZfP1fnDKw8MrL2ZWUgq32I08QeTeGOXclwt/K0tu1r4Dcc8a+Jq0OyiYWdhtRReRh++OC3+LFdbHpIdwHjX+tKW625UFgCO2oNzM4icWscrsRNGyHmy3Jo9+QdExdZmRYzNgJCxYlZeWdw3KnsNdSet11GthMsigSByEoZgVzwut3I+5j2u0ZDHkrYrIGFYBKCrbgWLHOthGkn4CZBk8QazgUZlbIAqGBNHvr4vtzvNm8nzB45BcsPPcAtkB9mrxraFn2m8DlSBCVAUSKQxmiyxLIe3c3p3LuSzO7s3dndyWZj9yf2B+/On26J0HdXIwNRwoUsfOx/1Ohm0oTbNIJZpDlIwkW4wGY+WFa3O4l3EyqclRpDwgPF4rQvz39Nmkw/CzHEOJKtkY2BIuPBPcEjR3Rg3I3B6S7RubM2Iahfcjjm7rnW22+4WaWBFTbJ1cKjQjIliVsgsarW2d/iPwkF2iS5LSSMuoNBWb/awGvsO1n9kGVhYuipsd/sfyBgc4Xwbj350WLMWZnZ2bkszMSWJ/tX//xAAtEQACAgECAwcCBwAAAAAAAAABAgMEEQAFBhIhBxMgIjFBURRAFTJCUmFxkf/aAAgBAgEBPwD7SnSs35hFXjZ2xk/wPk6tbPZqqTlW5PzAdCNcS7lPtO1SWIAveBlAz1AydbZxJuE0xP1Egl6nqeZTj1GqM7WacMzDBkjBOPkjw8FQzR3JLRjbuTGU5/bOQdcSRxTxo0TLlo3XOfXI1xzucnefgyQGR3wWZepyD6ADW31pFucoZVYZBVshv8OuGNykswGrImDXUAEe48NPdLlDZK8dcqgmlm53PsFA6A67Y9ytbf2fULtWyYbk80SPIpwzqynOuDd1E9yGaYpHLFKqd4R6hxriWaMbxUblKWY+UTMcebByCCNcJuHuWiP2Dw1r8kERhZVlhLcxifOM/Ix1GuNYm4x7Q4Kj99DUhoqGjR8qiqD6Z+TraEpmxuNeMBljlISQfqUHAJ03AUE8sMyXJAnlLK45jj4B1R2+rt8XJAmPk+pP9nxbtw9Jatvfo2jVtvXMDPyh1ZT8g++th7Nt0p3pfrLEawEDzRklm0ihEVR6KPtv/8QAKhEAAgEDAwMDAwUAAAAAAAAAAQIDBBESAAUGITFBEyBRFUBxBxQyQqH/2gAIAQMBAT8A+03HcqLa4PWq5VjQsACfJPjVJvVJV44kjP8Age4N/wAa4/t8O57nFTzFsCCTboTYX1uPH6GGJQYIzF0W4FmF+xuNVcIpqqaJTcI5A/APt/Uyop56CKhSVP3ImWT0r2ONiNcDkqKVqlJkkCrJC1iD0sTrhVDE8f1c1Cqkdwqt0HbuSdV1VG1LkVZkLDFlsV73BuNci29KecVCPcTsSQfB9u47Ft+68nrJqsSOaeGnwjUXuXNrkfA1woMOYbtt2OVJFmyRm5VCGGmEoQwxFyr90BPW3Xtrjkch2mqW6vTyZGED+txYgg65OhSmpQflvbW7TDVTrUpI8FQsZQTR98T4N+hGuO7T9C26ZWl9eWadpGlIszFj50XdMJFNiP8ADqDm9RFTPE9JGXN8WQ4i58karK6prpPUmfL4Hgfge5JcRiwuL31LUoyjEdft/wD/2Q==',
                                height: 90,
                                width: 95
                        }
                        ],
                                columnGap: 15
                        };
                        },
                        footer: function(page, pages) {
                        return {
                        columns: [
                                'IPN - DSE',
                        {
                        alignment: 'right',
                                text: [
                                { text: page.toString(), italics: true },
                                        ' de ',
                                { text: pages.toString(), italics: true },
                                        '\n',
                                { text: 'Fecha y hora de impresion: <s:date name="fechaHoy" format="dd/MM/yyyy HH:mm:ss"/>', italics: true, fontSize: 9}
                                ]
                        },
                        ],
                                margin: [10, 0]
                        };
                        },
                        content: [
                                ' ',
                        {
                        table: {
                        headerRows: 1,
                                widths: ['auto', 'auto', 'auto', '*', 'auto', 'auto', 'auto', 'auto', 'auto', 'auto', 'auto'],
                                body: [
                                        [{text: 'No', style: 'headerTable'}, {text: 'Boleta', style: 'headerTable'}, {text: 'CURP', style: 'headerTable'}, {text: 'Nombre', style: 'headerTable'}, {text: 'Genero', style: 'headerTable'}, {text: 'Semestre', style: 'headerTable'}, {text: 'Promedio', style: 'headerTable'}, {text: 'Tipo Beca', style: 'headerTable'}, {text: 'Fecha ini.', style: 'headerTable'}, {text: 'Fecha fin', style: 'headerTable'}, {text: 'Proceso', style: 'headerTable'}],
        <s:iterator value="resumenes">
            <s:property value="otorgamientoListStr" escape="false" />
        </s:iterator>

                                ]
                        }
                        },
                                'TOTAL: <s:property value="total" escape="false" />',
                                ' ', ' ', ' ',
                        {
                        table: {
                        headerRows: 0,
                                widths: ['*', 'auto', '*'],
                                body: [
                                        [
                                        {text: '\n\n\n\n\n__________________________________', style: 'firma'}, {text: '\n\n\n\n\n__________________________________', style: 'firma'}, {text: '\n\n\n\n\n__________________________________', style: 'firma'}

                                        ],
                                        [
                                        {text: '<s:property value="director.nombre" escape="false" /> <s:property value="director.apellidoPaterno" escape="false" /> <s:property value="director.apellidoMaterno" escape="false" />', style: 'firma'}, {text: '<s:property value="subdirector.nombre" escape="false" /> <s:property value="subdirector.apellidoPaterno" escape="false" /> <s:property value="subdirector.apellidoMaterno" escape="false" />', style: 'firma'}, {text: '<s:property value="responsable.nombre" escape="false" /> <s:property value="responsable.apellidoPaterno" escape="false" /> <s:property value="responsable.apellidoMaterno" escape="false" />', style: 'firma'}
                                                                            ],
                                                                            [
                                                                            {text: 'DIRECTOR(A) DE ESCUELA, CENTRO O UNIDAD ACADEMICA', style: 'firma'}, {text: 'SUBDIRECTOR(A) DE SERVICIOS ACADEMICOS E INTEGRACION SOCIAL', style: 'firma'}, {text: 'RESPONSABLE DE BECAS', style: 'firma'}
                                                                            ]
                                                                    ]
                                                            },
                                                                    layout: 'noBorders',
                                                                    pageBreak: 'after'
                                                            },
                                                                    ' ',
                                                            {
                                                            table: {
                                                            headerRows: 1,
                                                                    widths: ['*', '*', '*'],
                                                                    body: [
                                                                            [{text: 'Programa de Beca', style: 'headerTable'}, {text: 'Total de Becarios', style: 'headerTable'}, {text: 'Proceso', style: 'headerTable'}],
        <s:iterator value="resumenes">
            <s:property value="otorgamientosBecariosCountStr" escape="false" />
        </s:iterator>

                                                                    ]
                                                            }
                                                            },
                                                                    'TOTAL: <s:property value="total" escape="false" />',
                                                                    ' ', ' ', ' ',
                                                            {
                                                            table: {
                                                            headerRows: 0,
                                                                    widths: ['*', 'auto', '*'],
                                                                    body: [
                                                                            [
                                                                            {text: '\n\n\n\n\n__________________________________', style: 'firma'}, {text: '\n\n\n\n\n__________________________________', style: 'firma'}, {text: '\n\n\n\n\n__________________________________', style: 'firma'}

                                                                            ],
                                                                            [
                                                                            {text: '<s:property value="director.nombre" escape="false" /> <s:property value="director.apellidoPaterno" escape="false" /> <s:property value="director.apellidoMaterno" escape="false" />', style: 'firma'}, {text: '<s:property value="subdirector.nombre" escape="false" /> <s:property value="subdirector.apellidoPaterno" escape="false" /> <s:property value="subdirector.apellidoMaterno" escape="false" />', style: 'firma'}, {text: '<s:property value="responsable.nombre" escape="false" /> <s:property value="responsable.apellidoPaterno" escape="false" /> <s:property value="responsable.apellidoMaterno" escape="false" />', style: 'firma'}
                                                                                                                ],
                                                                                                                [
                                                                                                                {text: 'DIRECTOR(A) DE ESCUELA, CENTRO O UNIDAD ACADEMICA', style: 'firma'}, {text: 'SUBDIRECTOR(A) DE SERVICIOS ACADEMICOS E INTEGRACION SOCIAL', style: 'firma'}, {text: 'RESPONSABLE DE BECAS', style: 'firma'}
                                                                                                                ]
                                                                                                        ]
                                                                                                },
                                                                                                        layout: 'noBorders'
                                                                                                },
                                                                                                        ' ',
                                                                                                {
                                                                                                table: {
                                                                                                headerRows: 0,
                                                                                                        widths: ['*', '*', '*', '*'],
                                                                                                        body: [
                                                                                                                [
                                                                                                                {text: '\n\n\n\n\n_______________________', style: 'firma'}, {text: '\n\n\n\n\n_______________________', style: 'firma'}, {text: '\n\n\n\n\n_______________________', style: 'firma'}, {text: '\n\n\n\n\n_______________________', style: 'firma'}
                                                                                                                ],
                                                                                                                [
                                                                                                                {text: 'NOMBRE Y FIRMA', style: 'firma'}, {text: 'NOMBRE Y FIRMA', style: 'firma'}, {text: 'NOMBRE Y FIRMA', style: 'firma'}, {text: 'NOMBRE Y FIRMA', style: 'firma'}
                                                                                                                ]
                                                                                                        ]
                                                                                                },
                                                                                                        layout: 'noBorders'
                                                                                                }
                                                                                                ],
                                                                                                styles: {
                                                                                                headerTable: {
                                                                                                fontSize: 10,
                                                                                                        bold: true,
                                                                                                        fillColor: '#D8D8D8'
                                                                                                },
                                                                                                        datos: {
                                                                                                        fontSize: 8.5,
                                                                                                                color: '#2E2E2E'
                                                                                                        },
                                                                                                        firma: {
                                                                                                        fontSize: 9,
                                                                                                                color: '#2E2E2E',
                                                                                                                alignment: 'center'
                                                                                                        }
                                                                                                }
                                                                                        };
                                                                                                createPdf(docDefinition).open();
                                                                                        }
    </script>
    <script>
        (function (i, s, o, g, r, a, m) {
        i['GoogleAnalyticsObject'] = r;
                i[r] = i[r] || function () {
        (i[r].q = i[r].q || []).push(arguments)
        }, i[r].l = 1 * new Date();
                a = s.createElement(o),
                m = s.getElementsByTagName(o)[0];
                a.async = 1;
                a.src = g;
                m.parentNode.insertBefore(a, m)
        })(window, document, 'script', 'https://www.google-analytics.com/analytics.js', 'ga');
                ga('create', 'UA-83015806-1', 'auto');
                ga('send', 'pageview');</script>
</head>
<body class="theme-white">
    <div class="container">
        <h1>Imprimir</h1>
        <div class="row ">

            <div class="col-sm-12">
                <div class="main-box clearfix">
                    <p>Estamos preparando tu documento</p>
                </div>
            </div>
        </div>
    </div>

    <script type="text/javascript" src="/bootstrap-validator/js/bootstrapValidator.js"></script>
    <script type="text/javascript" language="javascript" class="init">
                $(document).ready(function () {
        $('#proceso').bootstrapValidator({});
                $('input[type=text]').addClass('form-control');
                $('select').addClass('form-control');
        });
    </script>
</body>