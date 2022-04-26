package br.org.flem.util.controller;

import br.org.flem.util.helper.StringUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tscortes
 */
@RestController
@RequestMapping("/utils")
public class UtilController {

    @GetMapping("cpfValido/{cpf}")
    public ResponseEntity<Boolean> validarCPF(@PathVariable("cpf") String cpf) throws Exception {
        try {
            return new ResponseEntity(StringUtil.validarCPF(cpf), HttpStatus.OK);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
    
    @GetMapping("cnpjValido/{cnpj}")
    public ResponseEntity<Boolean> validarCNPJ(@PathVariable("cnpj") String cnpj) throws Exception {
        try {
            return new ResponseEntity(StringUtil.validarCNPJ(cnpj), HttpStatus.OK);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
}
