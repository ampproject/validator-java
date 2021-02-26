package dev.amp.validator.visitor;

import dev.amp.validator.Context;
import dev.amp.validator.ValidatorProtos;
import dev.amp.validator.css.Declaration;
import dev.amp.validator.css.ParsedDocCssSpec;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class InvalidDeclVisitor implements RuleVisitor {

    /**
     * Visitor for "important" css element
     *
     * @param spec the parsed doc css spec
     * @param context the context
     * @param result validation result
     * @param tagDescriptiveName the descriptive name of a declaration
     */
    public InvalidDeclVisitor(@Nonnull final ParsedDocCssSpec spec, @Nonnull final Context context, final String tagDescriptiveName,
                              @Nonnull final ValidatorProtos.ValidationResult.Builder result) {
        super();
        this.spec = spec;
        this.context = context;
        this.result = result;
        this.tagDescriptiveName = tagDescriptiveName;
    }

    /**
     * visitDeclaration implementation
     *
     * @param declaration the declaration to visit
     */
    public void visitDeclaration(@Nonnull final Declaration declaration) {
        final ValidatorProtos.CssDeclaration cssDeclaration = this.spec.getCssDeclarationByName().get(declaration.getName());
        final String firstIdent = declaration.firstIdent();
        if (cssDeclaration == null) {
            List<String> params = new ArrayList<>();
            params.add(declaration.getName());
            params.add("style amp-custom");

            this.context.addError(
                    ValidatorProtos.ValidationError.Code.CSS_SYNTAX_INVALID_PROPERTY_NOLIST,
                    context.getLineCol().getLineNumber() + declaration.getLine(),
                    context.getLineCol().getColumnNumber() + declaration.getCol(),
                    params, this.spec.getSpec().getSpecUrl(),
                    this.result);
            return;
        }
        if (cssDeclaration.getValueCaseiList().size() > 0) {
            boolean hasValidValue = false;
            for (final String value : cssDeclaration.getValueCaseiList()) {
                if (firstIdent.toLowerCase() == value) {
                    hasValidValue = true;
                    break;
                }
            }
            if (!hasValidValue) {
                // Declaration value not allowed.
                List<String> params = new ArrayList<>();
                params.add(this.tagDescriptiveName);
                params.add(declaration.getName());
                params.add(firstIdent);

                this.context.addError(
                        ValidatorProtos.ValidationError.Code.CSS_SYNTAX_DISALLOWED_PROPERTY_VALUE,
                        context.getLineCol().getLineNumber() + declaration.getLine(),
                        context.getLineCol().getColumnNumber() + declaration.getCol(),
                        params,
                        this.spec.getSpec().getSpecUrl(), this.result);
            } else if (!cssDeclaration.hasValueRegexCasei()) {
                final Pattern valueRegex = this.context.getRules().getFullMatchCaseiRegex((cssDeclaration.getValueRegexCasei()));
                if (!valueRegex.matcher(firstIdent).matches()) {
                    List<String> params = new ArrayList<>();
                    params.add(this.tagDescriptiveName);
                    params.add(declaration.getName());
                    params.add(firstIdent);

                    this.context.addError(
                            ValidatorProtos.ValidationError.Code.CSS_SYNTAX_DISALLOWED_PROPERTY_VALUE,
                            context.getLineCol().getLineNumber() + declaration.getLine(),
                            context.getLineCol().getColumnNumber() + declaration.getCol(),
                            params, this.spec.getSpec().getSpecUrl(), this.result);
                }
            }
        }
    }

    /**
     * ParsedDocCssSpec associated to Visitor
     */
    private final ParsedDocCssSpec spec;

    /**
     * descriptive name associated to Visitor
     */
    private final String tagDescriptiveName;

    /**
     * reference to Context
     */
    private final Context context;

    /**
     * reference to result
     */
    private final ValidatorProtos.ValidationResult.Builder result;
}
