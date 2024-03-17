module.exports = {
  env: {
    amd: true,
    browser: true,
    node: true,
  },
  extends: [
    // 'react-app',
    // 'react-app/jest',
    'eslint:recommended',
    'plugin:typescript-sort-keys/recommended',
    'plugin:@typescript-eslint/eslint-recommended',
    'plugin:@typescript-eslint/recommended',
    'plugin:react/recommended',
    'plugin:react-hooks/recommended',
    'plugin:jsx-a11y/recommended',
    'plugin:prettier/recommended', // Make sure this is always the last element in the array.
  ],
  parser: '@typescript-eslint/parser',
  parserOptions: {
    ecmaFeatures: {
      jsx: true,
    },
    ecmaVersion: 2020,
    sourceType: 'module',
  },
  plugins: [
    'import',
    'simple-import-sort',
    'prettier',
    'unused-imports',
    'typescript-sort-keys',
    'sort-keys-fix',
    '@tanstack/query',
  ],
  root: true,
  rules: {
    // Checks effect dependencies
    '@typescript-eslint/ban-ts-comment': [
      'error',
      {
        minimumDescriptionLength: 3,
        'ts-check': false,
        'ts-expect-error': { descriptionFormat: '^: TS\\d+ because .+$' },
        'ts-ignore': { descriptionFormat: '^: TS\\d+ because .+$' },
        'ts-nocheck': { descriptionFormat: '^: TS\\d+ because .+$' },
      },
    ],

    '@typescript-eslint/explicit-function-return-type': 'off',

    '@typescript-eslint/naming-convention': [
      'error',
      {
        format: null,
        selector: 'default',
      },
      {
        format: ['camelCase', 'UPPER_CASE', 'PascalCase'],
        selector: 'variable',
      },
      {
        format: ['camelCase'],
        leadingUnderscore: 'allow',
        selector: 'parameter',
      },
      {
        format: ['camelCase'],
        leadingUnderscore: 'require',
        modifiers: ['private'],
        selector: 'memberLike',
      },
      {
        format: ['PascalCase'],
        selector: 'typeLike',
      },
    ],

    '@typescript-eslint/no-empty-interface': [
      'error',
      {
        allowSingleExtends: true,
      },
    ],

    '@typescript-eslint/no-non-null-asserted-optional-chain': 'warn',

    '@typescript-eslint/no-unused-vars': 'off',

    camelcase: 'off',

    'func-style': ['error', 'declaration', { allowArrowFunctions: true }],

    'import/newline-after-import': [
      'error',
      {
        count: 1,
      },
    ],

    'jsx-a11y/accessible-emoji': 'off',

    'jsx-a11y/anchor-is-valid': [
      'error',
      {
        aspects: ['preferButton'],
        components: ['Link'],
        specialLink: ['hrefLeft', 'hrefRight'],
      },
    ],

    'jsx-a11y/click-events-have-key-events': 'off',

    'jsx-a11y/label-has-associated-control': 'off',

    'jsx-a11y/no-autofocus': 'off',

    'jsx-a11y/no-static-element-interactions': 'off',

    'jsx-quotes': ['error', 'prefer-double'],

    'lines-around-comment': [
      'error',
      {
        allowArrayStart: true,
        allowBlockStart: true,
        allowClassStart: true,
        allowObjectStart: true,
        beforeBlockComment: true,
        beforeLineComment: true,
      },
    ],

    'newline-before-return': 'error',

    'no-duplicate-imports': 'error',

    'no-else-return': 'error',

    'no-restricted-imports': [
      'error',
      {
        patterns: ['.*'],
      },
    ],

    'no-unreachable': 'error',

    'no-unused-vars': 'off',

    'object-shorthand': 'error',

    'prefer-arrow-callback': 'error',

    'prefer-const': [
      'error',
      {
        destructuring: 'any',
        ignoreReadBeforeAssign: false,
      },
    ],

    'prettier/prettier': ['error', {}, { usePrettierrc: true }],

    'react-hooks/exhaustive-deps': 'warn',
    'react-hooks/rules-of-hooks': 'warn',
    'react/display-name': 'off',
    'react/jsx-boolean-value': ['error', 'never'],
    'react/jsx-pascal-case': [2, { allowAllCaps: true, allowNamespace: true }],
    'react/jsx-sort-props': ['error', { reservedFirst: true, shorthandLast: true }],
    'react/jsx-uses-react': 'error',
    'react/jsx-uses-vars': 'error',
    'react/no-unescaped-entities': 'off',
    'react/prefer-stateless-function': [2],
    'react/prop-types': 'off',
    'react/react-in-jsx-scope': 'off',
    'react/self-closing-comp': [
      'error',
      {
        component: true,
        html: true,
      },
    ],
    'require-await': 'error',
    'simple-import-sort/exports': 'error',
    'simple-import-sort/imports': 'error',
    'sort-keys-fix/sort-keys-fix': 'error',
    'typescript-sort-keys/interface': [
      'error',
      'asc',
      { caseSensitive: true, natural: false, requiredFirst: true },
    ],
    'unused-imports/no-unused-imports': 'error',

    'unused-imports/no-unused-vars': [
      'error',
      { args: 'after-used', argsIgnorePattern: '^_', vars: 'all', varsIgnorePattern: '^_' },
    ],
  },
  settings: {
    'import/resolver': {
      node: {
        extensions: ['.js', '.jsx', '.ts', '.tsx'],
        paths: ['src'],
      },
    },
    react: {
      version: 'detect',
    },
  },
};
